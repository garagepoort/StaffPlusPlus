package net.shortninja.staffplus.core.domain.staff.alerts;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.*;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.ONLINE;

@Command(
    command = "commands:commands.alerts",
    permissions = {"permissions:permissions.mention", "permissions:permissions.name-change", "permissions:permissions.xray"},
    description = "Enables or disables the alert type.",
    usage = "[namechange | mention | xray] {player} {enable | disable}",
    playerRetrievalStrategy = ONLINE
)
@IocBean
@IocMultiProvider(SppCommand.class)
public class AlertsCmd extends AbstractCmd {
    private final PermissionHandler permissionHandler;
    private final Options options;
    private final SessionManagerImpl sessionManager;

    public AlertsCmd(PermissionHandler permissionHandler, Messages messages, Options options, SessionManagerImpl sessionManager, CommandService commandService) {
        super(messages, permissionHandler, commandService);
        this.permissionHandler = permissionHandler;
        this.options = options;
        this.sessionManager = sessionManager;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        String alertType = args[0];
        if (args.length >= 3) {
            String option = args[2];
            handleAlertsArgument(sender, alertType, player.getPlayer(), false, option);
        } else if (args.length == 2) {
            handleAlertsArgument(sender, alertType, player.getPlayer(), false, "");
        } else {
            if ((!(sender instanceof Player))) {
                throw new BusinessException(messages.onlyPlayers);
            }
            handleAlertsArgument(sender, alertType, (Player) sender, true, "");
        }

        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            return 1;
        }
        return 2;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        if (args.length == 1 && sender instanceof Player) {
            return Optional.of(sender.getName());
        }
        return Optional.of(args[1]);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return Stream.of(AlertType.values())
                .map(Enum::name)
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }

        if (args.length == 2) {
            List<String> onlinePLayers = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
            return onlinePLayers.stream()
                .filter(s -> args[1].isEmpty() || s.contains(args[1]))
                .collect(Collectors.toList());
        }

        if (args.length == 3) {
            return Stream.of("enabled", "disabled")
                .filter(s -> args[2].isEmpty() || s.contains(args[2]))
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private void handleAlertsArgument(CommandSender sender, String alertTypeName, Player player, boolean shouldCheckPermission, String option) {
        if (alertTypeName.equals("namechange")) {
            alertTypeName = "name_change";
        }

        alertTypeName = alertTypeName.substring(0, 1).toUpperCase() + alertTypeName.substring(1);

        boolean isValid = JavaUtils.isValidEnum(AlertType.class, alertTypeName.toUpperCase());
        PlayerSession session = sessionManager.get(player.getUniqueId());

        if (!isValid) {
            sendHelp(sender);
            return;
        }

        AlertType alertType = AlertType.valueOf(alertTypeName.toUpperCase());
        boolean isEnabled = option.isEmpty() ? !session.shouldNotify(alertType) : option.equalsIgnoreCase("enable");
        boolean wasChanged = setAlertType(player, alertType, isEnabled, shouldCheckPermission);
        if (wasChanged && shouldCheckPermission) {
            messages.send(player, messages.alertChanged.replace("%alerttype%", alertTypeName.replace("_", " ")).replace("%status%", isEnabled ? "enabled" : "disabled"), messages.prefixGeneral);
        }
        sessionManager.saveSession(player);
    }

    private boolean setAlertType(Player player, AlertType alertType, boolean isEnabled, boolean shouldCheckPermission) {
        PlayerSession session = sessionManager.get(player.getUniqueId());
        if (this.permissionHandler.has(player, options.alertsConfiguration.getPermissionForType(alertType)) || !shouldCheckPermission) {
            session.setAlertOption(alertType, isEnabled);
            return true;
        }

        messages.send(player, messages.noPermission, messages.prefixGeneral);
        return false;
    }

    private void sendHelp(CommandSender sender) {
        messages.send(sender, "&7" + messages.LONG_LINE, "");
        messages.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixGeneral);
        messages.send(sender, "&7" + messages.LONG_LINE, "");
    }
}