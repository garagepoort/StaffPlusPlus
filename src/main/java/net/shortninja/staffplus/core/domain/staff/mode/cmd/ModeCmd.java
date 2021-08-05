package net.shortninja.staffplus.core.domain.staff.mode.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.ONLINE;

@Command(
    command = "commands:staff-mode",
    permissions = "permissions:mode",
    description = "Enables or disables staff mode.",
    usage = "[player] [enable | disable]",
    playerRetrievalStrategy = ONLINE
)
@IocBean
@IocMultiProvider(SppCommand.class)
public class ModeCmd extends AbstractCmd {
    private static final String ENABLE = "enable";
    private static final String DISABLE = "disable";
    private static final String MODE_TYPE = "-mode=";

    private final PermissionHandler permissionHandler;
    private final Options options;
    private final StaffModeService staffModeService;
    private final SessionManagerImpl sessionManager;
    private final PlayerManager playerManager;
    private final BukkitUtils bukkitUtils;

    @ConfigProperty("permissions:mode-specific")
    private String permissionModeSpecific;

    public ModeCmd(PermissionHandler permissionHandler,
                   Messages messages,
                   Options options,
                   StaffModeService staffModeService,
                   SessionManagerImpl sessionManager,
                   CommandService commandService,
                   PlayerManager playerManager, BukkitUtils bukkitUtils) {
        super(messages, permissionHandler, commandService);
        this.permissionHandler = permissionHandler;
        this.options = options;
        this.staffModeService = staffModeService;
        this.sessionManager = sessionManager;
        this.playerManager = playerManager;
        this.bukkitUtils = bukkitUtils;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer, Map<String, String> optionalParameters) {
        bukkitUtils.runTaskAsync(sender, () -> {
            if (args.length == 0) {
                validateIsPlayer(sender);
                toggleMode((Player) sender);
                return;
            }

            if (args.length == 1) {
                if (args[0].startsWith(MODE_TYPE)) {
                    turnOnSpecificStaffMode(sender, args[0]);
                } else {
                    permissionHandler.validateOp(sender);
                    toggleMode(targetPlayer.getPlayer());
                }
                return;
            }
            permissionHandler.validateOp(sender);
            String option = args[1];
            if (option.startsWith(MODE_TYPE)) {
                turnOnSpecificStaffMode(sender, args[0]);
            } else {
                switch (option) {
                    case ENABLE:
                        staffModeService.turnStaffModeOn(targetPlayer.getPlayer());
                        break;
                    case DISABLE:
                        staffModeService.turnStaffModeOff(targetPlayer.getPlayer());
                        break;
                    default:
                        throw new BusinessException(messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()), messages.prefixGeneral);
                }
            }
        });


        return true;
    }

    private void turnOnSpecificStaffMode(CommandSender sender, String arg) {
        validateIsPlayer(sender);
        permissionHandler.validate(sender, permissionModeSpecific);
        staffModeService.turnStaffModeOn((Player) sender, getModeName(arg));
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            return 0;
        }
        return 1;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        if (args.length == 0 && (sender instanceof Player)) {
            return Optional.of(sender.getName());
        }
        if (args.length == 1 && args[0].startsWith(MODE_TYPE)) {
            return Optional.of(sender.getName());
        }
        return Optional.of(args[0]);
    }

    private void toggleMode(Player player) {
        PlayerSession session = sessionManager.get(player.getUniqueId());
        if (session.isInStaffMode()) {
            staffModeService.turnStaffModeOff(player);
        } else {
            staffModeService.turnStaffModeOn(player);
        }
    }

    private String getModeName(String arg) {
        String[] templateParams = arg.split("=");
        if (templateParams.length != 2) {
            throw new BusinessException("&CInvalid mode name provided");
        }
        return templateParams[1];
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        String currentArg = args.length > 0 ? args[args.length - 1] : "";

        if (args.length == 1) {
            if (currentArg.startsWith("-")) {
                return options.modeConfigurations.keySet().stream()
                    .map(k -> MODE_TYPE + k)
                    .filter(s -> s.contains(currentArg))
                    .collect(Collectors.toList());
            }
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> currentArg.isEmpty() || s.contains(currentArg))
                .collect(Collectors.toList());
        }

        if (args.length == 2) {
            if (currentArg.startsWith("-")) {
                return options.modeConfigurations.keySet().stream()
                    .map(k -> MODE_TYPE + k)
                    .filter(s -> s.contains(currentArg))
                    .collect(Collectors.toList());
            }
            return Stream.of(ENABLE, DISABLE)
                .filter(s -> currentArg.isEmpty() || s.contains(currentArg))
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}