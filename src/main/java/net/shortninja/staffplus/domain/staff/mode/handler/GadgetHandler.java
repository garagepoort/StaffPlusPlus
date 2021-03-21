package net.shortninja.staffplus.domain.staff.mode.handler;

import be.garagepoort.staffplusplus.craftbukkit.common.IProtocol;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.JavaUtils;
import net.shortninja.staffplus.common.config.Messages;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.common.utils.MessageCoordinator;
import net.shortninja.staffplus.common.utils.PermissionHandler;
import net.shortninja.staffplus.domain.player.PlayerManager;
import net.shortninja.staffplus.domain.player.SppPlayer;
import net.shortninja.staffplus.domain.player.gui.CounterGui;
import net.shortninja.staffplus.domain.player.gui.hub.HubGui;
import net.shortninja.staffplus.domain.staff.examine.gui.ExamineGui;
import net.shortninja.staffplus.domain.staff.mode.item.CustomModuleConfiguration;
import net.shortninja.staffplus.domain.staff.vanish.VanishServiceImpl;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class GadgetHandler {
    private final static Map<UUID, Integer> lastRandomTeleport = new HashMap<UUID, Integer>();
    private final IProtocol versionProtocol = StaffPlus.get().versionProtocol;
    private final PermissionHandler permission = IocContainer.getPermissionHandler();
    private final MessageCoordinator message = IocContainer.getMessage();
    private final Options options = IocContainer.getOptions();
    private final Messages messages = IocContainer.getMessages();
    private final SessionManagerImpl sessionManager = IocContainer.getSessionManager();
    private final CpsHandler cpsHandler = StaffPlus.get().cpsHandler;
    private final VanishServiceImpl vanishServiceImpl = IocContainer.getVanishService();
    private final PlayerManager playerManager = IocContainer.getPlayerManager();

    public GadgetType getGadgetType(ItemStack item, String value) {
        if (options.modeConfiguration.getCompassModeConfiguration().getIdentifier().equals(value)) {
            return GadgetType.COMPASS;
        }
        if (options.modeConfiguration.getRandomTeleportModeConfiguration().getIdentifier().equals(value)) {
            return GadgetType.RANDOM_TELEPORT;
        }
        if (options.modeConfiguration.getVanishModeConfiguration().getIdentifier().equals(value)) {
            return GadgetType.VANISH;
        }
        if (options.modeConfiguration.getGuiModeConfiguration().getIdentifier().equals(value)) {
            return GadgetType.GUI_HUB;
        }
        if (options.modeConfiguration.getCounterModeConfiguration().getIdentifier().equals(value)) {
            return GadgetType.COUNTER;
        }
        if (options.modeConfiguration.getFreezeModeConfiguration().getIdentifier().equals(value)) {
            return GadgetType.FREEZE;
        }
        if (options.modeConfiguration.getCpsModeConfiguration().getIdentifier().equals(value)) {
            return GadgetType.CPS;
        }
        if (options.modeConfiguration.getExamineModeConfiguration().getIdentifier().equals(value)) {
            return GadgetType.EXAMINE;
        }
        if (options.modeConfiguration.getFollowModeConfiguration().getIdentifier().equals(value)) {
            return GadgetType.FOLLOW;
        }

        return GadgetType.CUSTOM;
    }

    public Optional<CustomModuleConfiguration> getModule(ItemStack item) {
        String identifier = versionProtocol.getNbtString(item);
        return options.customModuleConfigurations
            .stream()
            .filter(m -> m.getIdentifier().equals(identifier))
            .findFirst();
    }

    public void onCompass(Player player) {
        Vector vector = player.getLocation().getDirection();


        player.setVelocity(JavaUtils.makeVelocitySafe(vector.multiply(options.modeConfiguration.getCompassModeConfiguration().getVelocity())));
    }

    public void onRandomTeleport(Player player) {
        List<Player> onlinePlayers = playerManager.getOnlinePlayers()
            .stream()
            .filter(p -> !p.getUniqueId().equals(player.getUniqueId()) && !permission.has(p, options.permissionMember))
            .collect(Collectors.toList());


        if (onlinePlayers.isEmpty()) {
            message.send(player, messages.modeNotEnoughPlayers, messages.prefixGeneral);
            return;
        }

        Player currentPlayer = null;
        if (options.modeConfiguration.getRandomTeleportModeConfiguration().isRandom()) {
            Random random = new Random();
            currentPlayer = onlinePlayers.get(random.nextInt(onlinePlayers.size()));
        } else {
            UUID uuid = player.getUniqueId();
            int lastIndex = lastRandomTeleport.get(uuid) == null ? 0 : lastRandomTeleport.get(uuid);
            lastIndex = lastIndex + 1 < onlinePlayers.size() ? lastIndex + 1 : 0;
            currentPlayer = onlinePlayers.get(lastIndex);
            lastRandomTeleport.put(uuid, lastIndex);
        }

        message.send(player, messages.modeRandomTeleport, messages.prefixGeneral);
        player.teleport(currentPlayer);
    }

    public void onVanish(Player player, boolean shouldUpdateItem) {
        ItemStack item = player.getItemInHand();
        int slot = JavaUtils.getItemSlot(player.getInventory(), item);

        PlayerSession session = sessionManager.get(player.getUniqueId());
        if (session.getVanishType() == options.modeConfiguration.getModeVanish()) {
            vanishServiceImpl.removeVanish(player);
        } else {
            vanishServiceImpl.addVanish(player, options.modeConfiguration.getModeVanish());
        }

        if (shouldUpdateItem && item != null) {
            player.getInventory().remove(item);
            player.getInventory().setItem(slot, options.modeConfiguration.getVanishModeConfiguration().getModeVanishItem(session, options.modeConfiguration.getModeVanish()));
        }
    }

    public void onGuiHub(Player player) {
        new HubGui(player, options.modeConfiguration.getGuiModeConfiguration().getItem().getItemMeta().getDisplayName()).show(player);
    }

    public void onCounter(Player player) {
        new CounterGui(player, options.modeConfiguration.getCounterModeConfiguration().getTitle(), 0).show(player);
    }

    public void onCps(CommandSender sender, Player targetPlayer) {
        if (targetPlayer == null) {
            return;
        }

        cpsHandler.startTest(sender, targetPlayer);
    }

    public void onExamine(Player player, SppPlayer targetPlayer) {
        if (targetPlayer == null) {
            return;
        }

        new ExamineGui(player, targetPlayer, options.modeConfiguration.getExamineModeConfiguration().getModeExamineTitle()).show(player);
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

    public void executeModule(Player player, Player targetPlayer, CustomModuleConfiguration customModuleConfiguration, Map<String, String> placeholders) {
        String command = customModuleConfiguration.getAction()
            .replace("%clicker%", player.getName());
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            command = command.replace(entry.getKey(), entry.getValue());
        }
        if (targetPlayer != null) {
            command = command.replace("%clicked%", targetPlayer.getName());
        }

        switch (customModuleConfiguration.getType()) {
            case COMMAND_STATIC:
                Bukkit.dispatchCommand(player, command);
                break;
            case COMMAND_DYNAMIC:
                if (targetPlayer != null) {
                    Bukkit.dispatchCommand(player, command);
                }
                break;
            case COMMAND_CONSOLE:
                if (targetPlayer != null) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                } else
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                break;
            default:
                break;
        }
    }

    public void updateGadgets() {
        Set<UUID> modeUsers = IocContainer.getModeCoordinator().getModeUsers();

        for (UUID uuid : modeUsers) {
            Optional<Player> player = sessionManager.get(uuid).getPlayer();

            if (!player.isPresent()) {
                continue;
            }

            for (ItemStack item : player.get().getInventory().getContents()) {
                if (item == null) {
                    continue;
                }

                if (getGadgetType(item, versionProtocol.getNbtString(item)) == GadgetType.COUNTER) {
                    item.setAmount(options.modeConfiguration.getCounterModeConfiguration().isModeCounterShowStaffMode() ? modeUsers.size() : permission.getStaffCount());
                    break;
                }
            }
        }
    }

    public enum GadgetType {
        COMPASS, RANDOM_TELEPORT, VANISH, GUI_HUB, COUNTER, FREEZE, CPS, EXAMINE,
        FOLLOW, CUSTOM, NO_GADGET;
    }
}