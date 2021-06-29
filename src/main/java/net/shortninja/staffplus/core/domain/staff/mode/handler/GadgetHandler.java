package net.shortninja.staffplus.core.domain.staff.mode.handler;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.ban.config.BanConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplus.core.domain.player.gui.CounterGui;
import net.shortninja.staffplus.core.domain.player.gui.hub.HubGui;
import net.shortninja.staffplus.core.domain.staff.examine.gui.ExamineGui;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
import net.shortninja.staffplus.core.domain.staff.mode.item.CustomModuleConfiguration;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishServiceImpl;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

@IocBean
public class GadgetHandler {
    private final static Map<UUID, Integer> lastRandomTeleport = new HashMap<UUID, Integer>();

    private final IProtocolService protocolService;
    private final BanConfiguration banConfiguration;
    private final PermissionHandler permission;

    private final Options options;
    private final Messages messages;
    private final SessionManagerImpl sessionManager;
    private final CpsHandler cpsHandler;
    private final VanishServiceImpl vanishServiceImpl;
    private final PlayerManager playerManager;
    private final StaffModeService staffModeService;

    public GadgetHandler(IProtocolService protocolService, BanConfiguration banConfiguration, PermissionHandler permission, Options options, Messages messages, SessionManagerImpl sessionManager, CpsHandler cpsHandler, VanishServiceImpl vanishServiceImpl, PlayerManager playerManager, StaffModeService staffModeService) {
        this.protocolService = protocolService;
        this.banConfiguration = banConfiguration;
        this.permission = permission;
        this.options = options;
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.cpsHandler = cpsHandler;
        this.vanishServiceImpl = vanishServiceImpl;
        this.playerManager = playerManager;
        this.staffModeService = staffModeService;
    }

    public GadgetType getGadgetType(ItemStack item, String value) {
        if (options.staffItemsConfiguration.getCompassModeConfiguration().getIdentifier().equals(value)) {
            return GadgetType.COMPASS;
        }
        if (options.staffItemsConfiguration.getRandomTeleportModeConfiguration().getIdentifier().equals(value)) {
            return GadgetType.RANDOM_TELEPORT;
        }
        if (options.staffItemsConfiguration.getVanishModeConfiguration().getIdentifier().equals(value)) {
            return GadgetType.VANISH;
        }
        if (options.staffItemsConfiguration.getGuiModeConfiguration().getIdentifier().equals(value)) {
            return GadgetType.GUI_HUB;
        }
        if (options.staffItemsConfiguration.getCounterModeConfiguration().getIdentifier().equals(value)) {
            return GadgetType.COUNTER;
        }
        if (options.staffItemsConfiguration.getFreezeModeConfiguration().getIdentifier().equals(value)) {
            return GadgetType.FREEZE;
        }
        if (options.staffItemsConfiguration.getCpsModeConfiguration().getIdentifier().equals(value)) {
            return GadgetType.CPS;
        }
        if (options.staffItemsConfiguration.getExamineModeConfiguration().getIdentifier().equals(value)) {
            return GadgetType.EXAMINE;
        }
        if (options.staffItemsConfiguration.getFollowModeConfiguration().getIdentifier().equals(value)) {
            return GadgetType.FOLLOW;
        }

        return GadgetType.CUSTOM;
    }

    public Optional<CustomModuleConfiguration> getModule(ItemStack item) {
        String identifier = protocolService.getVersionProtocol().getNbtString(item);
        return options.customModuleConfigurations
            .stream()
            .filter(m -> m.getIdentifier().equals(identifier))
            .findFirst();
    }

    public void onCompass(Player player) {
        Vector vector = player.getLocation().getDirection();


        player.setVelocity(JavaUtils.makeVelocitySafe(vector.multiply(options.staffItemsConfiguration.getCompassModeConfiguration().getVelocity())));
    }

    public void onRandomTeleport(Player player) {
        List<Player> onlinePlayers = playerManager.getOnlinePlayers()
            .stream()
            .filter(p -> !p.getUniqueId().equals(player.getUniqueId()) && !permission.has(p, options.permissionMember))
            .collect(Collectors.toList());


        if (onlinePlayers.isEmpty()) {
            messages.send(player, messages.modeNotEnoughPlayers, messages.prefixGeneral);
            return;
        }

        Player currentPlayer = null;
        if (options.staffItemsConfiguration.getRandomTeleportModeConfiguration().isRandom()) {
            Random random = new Random();
            currentPlayer = onlinePlayers.get(random.nextInt(onlinePlayers.size()));
        } else {
            UUID uuid = player.getUniqueId();
            int lastIndex = lastRandomTeleport.get(uuid) == null ? 0 : lastRandomTeleport.get(uuid);
            lastIndex = lastIndex + 1 < onlinePlayers.size() ? lastIndex + 1 : 0;
            currentPlayer = onlinePlayers.get(lastIndex);
            lastRandomTeleport.put(uuid, lastIndex);
        }

        messages.send(player, messages.modeRandomTeleport, messages.prefixGeneral);
        player.teleport(currentPlayer);
    }

    public void onVanish(Player player, boolean shouldUpdateItem) {
        ItemStack item = player.getItemInHand();
        int slot = JavaUtils.getItemSlot(player.getInventory(), item);

        PlayerSession session = sessionManager.get(player.getUniqueId());
        GeneralModeConfiguration modeConfiguration = session.getModeConfiguration().get();
        if (session.getVanishType() == modeConfiguration.getModeVanish()) {
            vanishServiceImpl.removeVanish(player);
        } else {
            vanishServiceImpl.addVanish(player, modeConfiguration.getModeVanish());
        }

        if (shouldUpdateItem && item != null) {
            player.getInventory().remove(item);
            player.getInventory().setItem(slot, options.staffItemsConfiguration.getVanishModeConfiguration().getModeVanishItem(session, modeConfiguration.getModeVanish()));
        }
    }

    public void onGuiHub(Player player) {
        new HubGui(player, options.staffItemsConfiguration.getGuiModeConfiguration().getItem().getItemMeta().getDisplayName(), banConfiguration).show(player);
    }

    public void onCounter(Player player) {
        new CounterGui(player, options.staffItemsConfiguration.getCounterModeConfiguration().getTitle(), 0).show(player);
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

        new ExamineGui(player, targetPlayer, options.staffItemsConfiguration.getExamineModeConfiguration().getModeExamineTitle()).show(player);
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
        Set<UUID> modeUsers = staffModeService.getModeUsers();

        for (UUID uuid : modeUsers) {
            Optional<Player> player = sessionManager.get(uuid).getPlayer();

            if (!player.isPresent()) {
                continue;
            }

            for (ItemStack item : player.get().getInventory().getContents()) {
                if (item == null) {
                    continue;
                }

                if (getGadgetType(item, protocolService.getVersionProtocol().getNbtString(item)) == GadgetType.COUNTER) {
                    item.setAmount(options.staffItemsConfiguration.getCounterModeConfiguration().isModeCounterShowStaffMode() ? modeUsers.size() : permission.getStaffCount());
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