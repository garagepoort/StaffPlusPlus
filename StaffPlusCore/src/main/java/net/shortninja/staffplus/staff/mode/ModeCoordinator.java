package net.shortninja.staffplus.staff.mode;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.InventorySerializer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplus.staff.mode.config.ModeItemConfiguration;
import net.shortninja.staffplus.staff.mode.config.gui.GuiConfiguration;
import net.shortninja.staffplus.staff.mode.config.modeitems.vanish.VanishModeConfiguration;
import net.shortninja.staffplus.staff.vanish.VanishHandler;
import net.shortninja.staffplus.unordered.VanishType;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModeCoordinator {
    private static final Map<UUID, InventoryVault> staffMembersSavedData = new HashMap<>();
    private final Logger logger = StaffPlus.get().getLogger();

    private final MessageCoordinator message;
    private final Options options;
    private final Messages messages;
    private final SessionManager sessionManager;
    private final VanishHandler vanishHandler;
    private final PermissionHandler permissionHandler;

    private final List<ModeItemConfiguration> MODE_ITEMS;
    private final GeneralModeConfiguration modeConfiguration;

    public ModeCoordinator(MessageCoordinator message, Options options, Messages messages, SessionManager sessionManager, VanishHandler vanishHandler, PermissionHandler permissionHandler) {
        this.message = message;
        this.options = options;
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.vanishHandler = vanishHandler;

        MODE_ITEMS = Arrays.asList(
            options.modeConfiguration.getCompassModeConfiguration(),
            options.modeConfiguration.getRandomTeleportModeConfiguration(),
            options.modeConfiguration.getVanishModeConfiguration(),
            options.modeConfiguration.getGuiModeConfiguration(),
            options.modeConfiguration.getCounterModeConfiguration(),
            options.modeConfiguration.getFreezeModeConfiguration(),
            options.modeConfiguration.getCpsModeConfiguration(),
            options.modeConfiguration.getExamineModeConfiguration(),
            options.modeConfiguration.getFollowModeConfiguration()
        );
        this.permissionHandler = permissionHandler;
        modeConfiguration = options.modeConfiguration;
    }

    public Set<UUID> getModeUsers() {
        return staffMembersSavedData.keySet();
    }

    public boolean isInMode(UUID uuid) {
        return staffMembersSavedData.containsKey(uuid);
    }

    public void addMode(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerSession session = sessionManager.get(uuid);
        if (isInMode(player.getUniqueId())) {
            return;
        }

        InventoryVault modeData = new InventoryVault(uuid, getContents(player), player.getInventory().getArmorContents(), player.getInventory().getExtraContents(),
            player.getLocation(), player.getExp(), player.getAllowFlight(), player.getGameMode(), session.getVanishType());
        staffMembersSavedData.put(uuid, modeData);

        JavaUtils.clearInventory(player);
        setPassive(player, session);
        message.send(player, messages.modeStatus.replace("%status%", messages.enabled), messages.prefixGeneral);
    }

    public void removeMode(Player player) {
        if (!isInMode(player.getUniqueId())) {
            return;
        }

        unsetPassive(player);
        staffMembersSavedData.remove(player.getUniqueId());
        message.send(player, messages.modeStatus.replace("%status%", messages.disabled), messages.prefixGeneral);
    }

    private void setPassive(Player player, PlayerSession session) {
        if (modeConfiguration.isModeFlight() && !modeConfiguration.isModeCreative()) {
            player.setAllowFlight(true);
        } else if (modeConfiguration.isModeCreative()) {
            player.setGameMode(GameMode.CREATIVE);
        }

        runModeCommands(player, true);
        vanishHandler.addVanish(player, modeConfiguration.getModeVanish());


        if (permissionHandler.isOp(player) || modeConfiguration.getStaffGuiConfigurations().isEmpty()) {
            getAllModeItems().forEach(modeItem -> addModeItem(player, session, modeItem, modeItem.getSlot()));
        } else {
            Optional<GuiConfiguration> applicableGui = modeConfiguration.getStaffGuiConfigurations().stream()
                .filter(gui -> permissionHandler.has(player, gui.getPermission()))
                .findFirst();

            if (!applicableGui.isPresent()) {
                logger.warning("No gui configuration found for player " + player.getName() + ". Make sure this player has one of the staff mode rank permissions");
                return;
            }

            applicableGui.get().getItemSlots().forEach((moduleName, slot) -> {
                Optional<ModeItemConfiguration> module = getModule(moduleName);
                if (!module.isPresent()) {
                    logger.warning("No module found with name [" + moduleName + "]. Skipping...");
                } else {
                    addModeItem(player, session, module.get(), slot);
                }
            });
        }
    }

    private void addModeItem(Player player, PlayerSession session, ModeItemConfiguration modeItem, int slot) {
        if (!modeItem.isEnabled()) {
            return;
        }

        if (modeItem instanceof VanishModeConfiguration) {
            player.getInventory().setItem(slot, ((VanishModeConfiguration) modeItem).getModeVanishItem(session, modeConfiguration.getModeVanish()));
        } else {
            player.getInventory().setItem(slot, modeItem.getItem());
        }
    }

    private Optional<ModeItemConfiguration> getModule(String name) {
        return getAllModeItems().stream().filter(m -> m.getIdentifier().equals(name)).findFirst();
    }

    private List<ModeItemConfiguration> getAllModeItems() {
        return Stream.of(MODE_ITEMS, options.customModuleConfigurations)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private void unsetPassive(Player player) {
        UUID uuid = player.getUniqueId();
        InventoryVault modeData = staffMembersSavedData.get(uuid);
        InventorySerializer saver = new InventorySerializer(player.getUniqueId());

        if (modeConfiguration.isModeOriginalLocation()) {
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
        for (String command : isEnabled ? modeConfiguration.getModeEnableCommands() : modeConfiguration.getModeDisableCommands()) {
            if (command.isEmpty()) {
                continue;
            }

            CommandSender target = (command.trim().startsWith("%player%")) ? player : Bukkit.getConsoleSender();
            command = (command.trim().startsWith("%player%)")) ? command.replaceFirst("%player%", "").trim() : command;
            Bukkit.dispatchCommand(target, command.replace("%player%", player.getName()));
        }
    }

    public static ItemStack[] getContents(Player p) {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        for (int i = 0; i <= 35; i++) {
            itemStacks.add(p.getInventory().getItem(i));
        }
        return itemStacks.toArray(new ItemStack[]{});
    }

    private void getItems(Player p, InventorySerializer saver) {
        ItemStack[] contents = saver.getContents();
        for (int i = 0; i < contents.length; i++) {
            p.getInventory().setItem(i, contents[i]);
        }
    }

}