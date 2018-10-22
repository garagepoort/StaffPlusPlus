package net.shortninja.staffplus.player.attribute.mode.handler;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.gui.CounterGui;
import net.shortninja.staffplus.player.attribute.gui.ExamineGui;
import net.shortninja.staffplus.player.attribute.gui.hub.HubGui;
import net.shortninja.staffplus.player.attribute.mode.item.ModeItem;
import net.shortninja.staffplus.player.attribute.mode.item.ModuleConfiguration;
import net.shortninja.staffplus.server.compatibility.IProtocol;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public class GadgetHandler {
    private static Map<UUID, Integer> lastRandomTeleport = new HashMap<UUID, Integer>();
    private IProtocol versionProtocol = StaffPlus.get().versionProtocol;
    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private UserManager userManager = StaffPlus.get().userManager;
    private CpsHandler cpsHandler = StaffPlus.get().cpsHandler;
    private FreezeHandler freezeHandler = StaffPlus.get().freezeHandler;

    public GadgetType getGadgetType(ItemStack item, String value) {
        GadgetType gadgetType = GadgetType.CUSTOM;

        if (value == null) {
            return gadgetType;
        }

        switch (value) {
            case "compass":
                gadgetType = GadgetType.COMPASS;
                break;
            case "randomTeleport":
                gadgetType = GadgetType.RANDOM_TELEPORT;
                break;
            case "vanish":
                gadgetType = GadgetType.VANISH;
                break;
            case "guiHub":
                gadgetType = GadgetType.GUI_HUB;
                break;
            case "counter":
                gadgetType = GadgetType.COUNTER;
                break;
            case "freeze":
                gadgetType = GadgetType.FREEZE;
                break;
            case "cps":
                gadgetType = GadgetType.CPS;
                break;
            case "examine":
                gadgetType = GadgetType.EXAMINE;
                break;
            case "follow":
                gadgetType = GadgetType.FOLLOW;
                break;
        }

        return gadgetType;
    }

    public ModuleConfiguration getModule(ItemStack item) {
        return options.moduleConfigurations.get(versionProtocol.getNbtString(item));
    }

    public void onCompass(Player player) {
        Vector vector = player.getLocation().getDirection();

        player.setVelocity(vector.multiply(options.modeCompassVelocity));
    }

    public void onRandomTeleport(Player player, int count) {
        List<Player> onlinePlayers = JavaUtils.getOnlinePlayers();
        Player currentPlayer = null;

        if (onlinePlayers.size() == 1) {
            message.send(player, messages.modeNotEnoughPlayers, messages.prefixGeneral);
            return;
        }

        if (options.modeRandomTeleportRandom) {
            Random random = new Random();

            do {
                currentPlayer = onlinePlayers.get(random.nextInt(onlinePlayers.size()));
            }
            while (player.getName().equals(currentPlayer.getName()) || permission.has(currentPlayer, options.permissionMember));
        } else {
            UUID uuid = player.getUniqueId();
            int lastIndex = lastRandomTeleport.get(uuid) == null ? 0 : lastRandomTeleport.get(uuid);

            if ((lastIndex + 1) < onlinePlayers.size()) {
                lastIndex++;
            } else lastIndex = 0;

            currentPlayer = onlinePlayers.get(lastIndex);

            if (count >= onlinePlayers.size()) {
                message.send(player, messages.modeNotEnoughPlayers, messages.prefixGeneral);
                return;
            } else if (player.getName().equals(currentPlayer.getName()) || permission.has(currentPlayer, options.permissionMember)) {
                lastRandomTeleport.put(uuid, lastIndex);
                onRandomTeleport(player, count + 1);
                return;
            }

            lastRandomTeleport.put(uuid, lastIndex);
        }

        message.send(player, messages.modeRandomTeleport, messages.prefixGeneral);
        player.teleport(currentPlayer);
    }

    public void onVanish(Player player, boolean shouldUpdateItem) {
        ModeItem modeItem = StaffPlus.get().modeCoordinator.MODE_ITEMS[2];
        ItemStack item = player.getItemInHand();
        int slot = JavaUtils.getItemSlot(player.getInventory(), item);

        if (userManager.get(player.getUniqueId()).getVanishType() == options.modeVanish) {
            StaffPlus.get().vanishHandler.removeVanish(player);

            if (shouldUpdateItem && item != null) {
                player.getInventory().remove(item);
                player.getInventory().setItem(slot, versionProtocol.addNbtString(options.modeVanishItemOff, modeItem.getIdentifier()));
            }
        } else {
            StaffPlus.get().vanishHandler.addVanish(player, options.modeVanish);

            if (shouldUpdateItem && item != null) {
                player.getInventory().remove(item);
                player.getInventory().setItem(slot, versionProtocol.addNbtString(options.modeVanishItem, modeItem.getIdentifier()));
            }
        }
    }

    public void onGuiHub(Player player) {
        new HubGui(player, options.modeGuiItem.getItemMeta().getDisplayName());
    }

    public void onCounter(Player player) {
        new CounterGui(player, options.modeCounterTitle);
    }

    public void onFreeze(CommandSender sender, Player targetPlayer) {
        if (targetPlayer == null) {
            return;
        }

        if (freezeHandler.isFrozen(targetPlayer.getUniqueId())) {
            freezeHandler.removeFreeze(sender, targetPlayer, true);
        } else freezeHandler.addFreeze(sender, targetPlayer, true);
    }

    public void onCps(CommandSender sender, Player targetPlayer) {
        if (targetPlayer == null) {
            return;
        }

        cpsHandler.startTest(sender, targetPlayer);
    }

    public void onExamine(Player player, Player targetPlayer) {
        if (targetPlayer == null) {
            return;
        }

        new ExamineGui(player, targetPlayer, options.modeExamineTitle);
    }

    public void onFollow(Player player, Player targetPlayer) {
        if (targetPlayer == null || player.getName().equals(targetPlayer.getName())) {
            return;
        }

        if (player.getVehicle() != null) {
            player.getVehicle().eject();
            return;
        }

        targetPlayer.setPassenger(player);
    }

    public void onCustom(Player player, Player targetPlayer, ModuleConfiguration moduleConfiguration) {
        switch (moduleConfiguration.getType()) {
            case COMMAND_STATIC:
                Bukkit.dispatchCommand(player, moduleConfiguration.getAction());
                break;
            case COMMAND_DYNAMIC:
                if (targetPlayer != null) {
                    Bukkit.dispatchCommand(player, moduleConfiguration.getAction().replace("%clicker%", player.getName()).replace("%clicked%", targetPlayer.getName()));
                }
                break;
            case COMMAND_CONSOLE:
                if (targetPlayer != null) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), moduleConfiguration.getAction().replace("%clicker%", player.getName()).replace("%clicked%", targetPlayer.getName()));
                } else
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), moduleConfiguration.getAction().replace("%clicker%", player.getName()));
                break;
            default:
                break;
        }
    }

    public void updateGadgets() {
        Set<UUID> modeUsers = StaffPlus.get().modeCoordinator.getModeUsers();

        for (UUID uuid : modeUsers) {
            Player player = userManager.get(uuid).getPlayer();

            if (player == null) {
                continue;
            }

            for (ItemStack item : player.getInventory().getContents()) {
                if (item == null) {
                    continue;
                }

                if (getGadgetType(item, versionProtocol.getNbtString(item)) == GadgetType.COUNTER) {
                    item.setAmount(options.modeCounterShowStaffMode ? modeUsers.size() : permission.getStaffCount());
                    break;
                }
            }
        }
    }

    public enum GadgetType {
        COMPASS, RANDOM_TELEPORT, VANISH, GUI_HUB, COUNTER, FREEZE, CPS, EXAMINE,
        FOLLOW, CUSTOM;
    }
}