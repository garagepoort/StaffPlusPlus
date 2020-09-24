package net.shortninja.staffplus.player.attribute.mode;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.player.attribute.InventorySerializer;
import net.shortninja.staffplus.player.attribute.mode.item.ModeItem;
import net.shortninja.staffplus.player.attribute.mode.item.ModuleConfiguration;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.vanish.VanishHandler;
import net.shortninja.staffplus.unordered.IPlayerSession;
import net.shortninja.staffplus.unordered.VanishType;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ModeCoordinator {
    private static Map<UUID, ModeDataVault> modeUsers = new HashMap<UUID, ModeDataVault>();
    private final MessageCoordinator message = IocContainer.getMessage();
    private final Options options = IocContainer.getOptions();
    public final ModeItem[] MODE_ITEMS =
            {
                    new ModeItem("compass", options.modeCompassItem, options.modeCompassSlot, options.modeCompassEnabled),
                    new ModeItem("randomTeleport", options.modeRandomTeleportItem, options.modeRandomTeleportSlot, options.modeRandomTeleportEnabled),
                    new ModeItem("vanish", options.modeVanishItem, options.modeVanishSlot, options.modeVanishEnabled),
                    new ModeItem("guiHub", options.modeGuiItem, options.modeGuiSlot, options.modeGuiEnabled),
                    new ModeItem("counter", options.modeCounterItem, options.modeCounterSlot, options.modeCounterEnabled),
                    new ModeItem("freeze", options.modeFreezeItem, options.modeFreezeSlot, options.modeFreezeEnabled),
                    new ModeItem("cps", options.modeCpsItem, options.modeCpsSlot, options.modeCpsEnabled),
                    new ModeItem("examine", options.modeExamineItem, options.modeExamineSlot, options.modeExamineEnabled),
                    new ModeItem("follow", options.modeFollowItem, options.modeFollowSlot, options.modeFollowEnabled),
            };

    private final Messages messages = IocContainer.getMessages();
    private final SessionManager sessionManager = IocContainer.getSessionManager();
    private final VanishHandler vanishHandler = IocContainer.getVanishHandler();

    public Set<UUID> getModeUsers() {
        return modeUsers.keySet();
    }

    public boolean isInMode(UUID uuid) {
        if(modeUsers == null)
            modeUsers = new HashMap<>();
        return modeUsers.containsKey(uuid);
    }

    public void addMode(Player player) {
        UUID uuid = player.getUniqueId();
        IPlayerSession session = sessionManager.get(uuid);
        ModeDataVault modeData;
        modeData = new ModeDataVault(uuid, getContents(player), player.getInventory().getArmorContents(), player.getInventory().getExtraContents(),
                player.getLocation(), player.getExp(), player.getAllowFlight(), player.getGameMode(), session.getVanishType());
        if (isInMode(player.getUniqueId())) {
            return;
        }

        JavaUtils.clearInventory(player);
        modeUsers.put(uuid, modeData);
        setPassive(player, session);
        message.send(player, messages.modeStatus.replace("%status%", messages.enabled), messages.prefixGeneral);
    }

    public void removeMode(Player player) {
        if (!isInMode(player.getUniqueId())) {
            return;
        }

        unsetPassive(player);
        modeUsers.remove(player.getUniqueId());
        message.send(player, messages.modeStatus.replace("%status%", messages.disabled), messages.prefixGeneral);
    }

    private void setPassive(Player player, IPlayerSession session) {
        if (options.modeFlight && !options.modeCreative) {
            player.setAllowFlight(true);
        } else if (options.modeCreative) {
            player.setGameMode(GameMode.CREATIVE);
        }

        runModeCommands(player, true);
        vanishHandler.addVanish(player, options.modeVanish);

        for (ModeItem modeItem : MODE_ITEMS) {
            if (!modeItem.isEnabled()) {
                continue;
            }

            if (modeItem.getIdentifier().equals("vanish")) {
                modeItem.setItem(session.getVanishType() == options.modeVanish ? options.modeVanishItem : options.modeVanishItemOff);
            }

            player.getInventory().setItem(modeItem.getSlot(), StaffPlus.get().versionProtocol.addNbtString(modeItem.getItem(), modeItem.getIdentifier()));
        }

        for (ModuleConfiguration moduleConfiguration : options.moduleConfigurations.values()) {
            player.getInventory().setItem(moduleConfiguration.getSlot(), StaffPlus.get().versionProtocol.addNbtString(moduleConfiguration.getItem(), moduleConfiguration.getIdentifier()));
        }
    }

    private void unsetPassive(Player player) {
        UUID uuid = player.getUniqueId();
        ModeDataVault modeData = modeUsers.get(uuid);
        InventorySerializer saver = new InventorySerializer(player.getUniqueId());

        if (options.modeOriginalLocation) {
            player.teleport(modeData.getPreviousLocation().setDirection(player.getLocation().getDirection()));
            message.send(player, messages.modeOriginalLocation, messages.prefixGeneral);
        }

        runModeCommands(player, false);
        JavaUtils.clearInventory(player);
        getItems(player, saver);
        player.setExp(saver.getXp());
        player.getInventory().setArmorContents(saver.getArmor());
        player.getInventory().setExtraContents(saver.getOffHand());

        saver.deleteFile();
        player.updateInventory();
        player.setAllowFlight(modeData.hasFlight());
        player.setGameMode(modeData.getGameMode());

        if (modeData.getVanishType() == VanishType.NONE) {
            vanishHandler.removeVanish(player);
        } else vanishHandler.addVanish(player, modeData.getVanishType());
    }

    private void runModeCommands(Player player, boolean isEnabled) {
        for (String command : isEnabled ? options.modeEnableCommands : options.modeDisableCommands) {
            if (command.isEmpty()) {
                continue;
            }

            CommandSender target = (command.trim().startsWith("%player%")) ? player : Bukkit.getConsoleSender();
            command = (command.trim().startsWith("%player%)")) ? command.replaceFirst("%player%", "").trim() : command;
            Bukkit.dispatchCommand(target, command.replace("%player%", player.getName()));
        }
    }

    public static HashMap<Integer, ItemStack> getContents(Player p) {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        HashMap<Integer, ItemStack> itemHash = new HashMap<>();
        for (int i = 0; i <= 35; i++) {
            if (p.getInventory().getItem(i) != null) {
                itemStacks.add(p.getInventory().getItem(i));
                itemHash.put(i, p.getInventory().getItem(i));
            }
        }
        return itemHash;
    }

    private void getItems(Player p, InventorySerializer saver){
        HashMap<String, ItemStack> items = saver.getContents();
        for(String num : items.keySet())
            p.getInventory().setItem(Integer.parseInt(num),items.get(num));

    }

}