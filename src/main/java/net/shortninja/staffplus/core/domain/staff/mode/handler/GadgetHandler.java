package net.shortninja.staffplus.core.domain.staff.mode.handler;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubinggui.GuiActionBuilder;
import be.garagepoort.mcioc.tubinggui.GuiActionService;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.mode.ModeProvider;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeItemsService;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.custommodules.CustomModuleConfiguration;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishService;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@IocBean
public class GadgetHandler {
    private final static Map<UUID, Integer> lastRandomTeleport = new HashMap<>();

    @ConfigProperty("permissions:random-teleport-bypass")
    private String randomTeleportBypass;

    private final IProtocolService protocolService;
    private final PermissionHandler permission;

    private final Options options;
    private final Messages messages;
    private final OnlineSessionsManager sessionManager;
    private final PlayerSettingsRepository playerSettingsRepository;
    private final CpsHandler cpsHandler;
    private final VanishService vanishService;
    private final PlayerManager playerManager;
    private final GuiActionService guiActionService;
    private final BukkitUtils bukkitUtils;
    private final ModeProvider modeProvider;
    private final StaffModeItemsService staffModeItemsService;

    public GadgetHandler(IProtocolService protocolService,
                         PermissionHandler permission,
                         Options options,
                         Messages messages,
                         OnlineSessionsManager sessionManager,
                         PlayerSettingsRepository playerSettingsRepository, CpsHandler cpsHandler,
                         VanishService vanishService,
                         PlayerManager playerManager,
                         GuiActionService guiActionService, BukkitUtils bukkitUtils, ModeProvider modeProvider, StaffModeItemsService staffModeItemsService) {
        this.protocolService = protocolService;
        this.permission = permission;
        this.options = options;
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.playerSettingsRepository = playerSettingsRepository;
        this.cpsHandler = cpsHandler;
        this.vanishService = vanishService;
        this.playerManager = playerManager;
        this.guiActionService = guiActionService;
        this.bukkitUtils = bukkitUtils;
        this.modeProvider = modeProvider;
        this.staffModeItemsService = staffModeItemsService;
    }

    public GadgetType getGadgetType(String value) {
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
        if (options.staffItemsConfiguration.getPlayerDetailsModeConfiguration().getIdentifier().equals(value)) {
            return GadgetType.PLAYER_DETAILS;
        }

        return GadgetType.CUSTOM;
    }

    public void onCompass(Player player) {
        Vector vector = player.getLocation().getDirection();


        player.setVelocity(JavaUtils.makeVelocitySafe(vector.multiply(options.staffItemsConfiguration.getCompassModeConfiguration().getVelocity())));
    }

    public void onRandomTeleport(Player player) {
        List<Player> onlinePlayers = playerManager.getOnlinePlayers()
            .stream()
            .filter(p -> !p.getUniqueId().equals(player.getUniqueId()) && !permission.has(p, randomTeleportBypass))
            .collect(Collectors.toList());


        if (onlinePlayers.isEmpty()) {
            messages.send(player, messages.modeNotEnoughPlayers, messages.prefixGeneral);
            return;
        }

        Player currentPlayer;
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

        messages.send(player, messages.modeRandomTeleport.replace("%target%", currentPlayer.getName()), messages.prefixGeneral);
        player.teleport(currentPlayer);
    }

    public void onVanish(Player player) {
        PlayerSettings settings = playerSettingsRepository.get(player);
        GeneralModeConfiguration modeConfiguration = modeProvider.getMode(player, settings.getModeName().get());

        bukkitUtils.runTaskAsync(player, () -> {
            if (settings.getVanishType() == modeConfiguration.getModeVanish()) {
                vanishService.removeVanish(player);
            } else {
                vanishService.addVanish(player, modeConfiguration.getModeVanish());
            }
        });
    }

    public void onGuiHub(Player player) {
        guiActionService.executeAction(player, "hub/view");
    }

    public void onCounter(Player player) {
        guiActionService.executeAction(player, "membersGUI");
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

        guiActionService.executeAction(player, GuiActionBuilder.builder()
            .action("examine/view")
            .param("targetPlayerName", targetPlayer.getUsername())
            .build());
    }

    public void onFollow(Player player, Player targetPlayer) {
        if (targetPlayer == null || player.equals(targetPlayer)) {
            return;
        }

        if (player.getVehicle() != null) {
            player.getVehicle().eject();
        }
        
        targetPlayer.addPassenger(player);
    }

    public void executeModule(Player player, Player targetPlayer, CustomModuleConfiguration customModuleConfiguration, Map<String, String> placeholders) {
        List<String> commands = customModuleConfiguration.getActions();
        for (String action : commands) {
            String command = action.replace("%clicker%", player.getName());
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
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    break;
                default:
                    break;
            }
        }
    }

    public void updateGadgets() {
        Set<Player> modeUsers = getModeUsers();

        for (Player player : modeUsers) {
            for (ItemStack item : player.getInventory().getContents()) {
                if (item == null) {
                    continue;
                }

                if (getGadgetType(protocolService.getVersionProtocol().getNbtString(item)) == GadgetType.COUNTER) {
                    item.setAmount(options.staffItemsConfiguration.getCounterModeConfiguration().isModeCounterShowStaffMode() ? modeUsers.size() : permission.getStaffCount());
                    break;
                }
            }
        }
    }

    private Set<Player> getModeUsers() {
        return sessionManager.getAll().stream()
            .filter(OnlinePlayerSession::isInStaffMode)
            .map(s -> playerManager.getOnlinePlayer(s.getUuid()))
            .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
            .map(SppPlayer::getPlayer)
            .collect(Collectors.toSet());
    }
}