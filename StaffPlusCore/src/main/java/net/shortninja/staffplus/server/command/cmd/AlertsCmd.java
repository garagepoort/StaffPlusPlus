package net.shortninja.staffplus.server.command.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.unordered.AlertType;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class AlertsCmd extends AbstractCmd {
    private MessageCoordinator message = IocContainer.getMessage();
    private SessionManager sessionManager = IocContainer.getSessionManager();

    public AlertsCmd(String name) {
        super(name, IocContainer.getOptions().permissionAlerts);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
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
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.ONLINE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        if (args.length == 1 && sender instanceof Player) {
            return Optional.of(sender.getName());
        }
        return Optional.of(args[1]);
    }

    private void handleAlertsArgument(CommandSender sender, String alertTypeName, Player player, boolean shouldCheckPermission, String option) {
        if (alertTypeName.equals("namechange")) {
            alertTypeName = "name_change";
        }

        alertTypeName = alertTypeName.substring(0, 1).toUpperCase() + alertTypeName.substring(1);

        boolean wasChanged = false;
        boolean isValid = JavaUtils.isValidEnum(AlertType.class, alertTypeName.toUpperCase());
        PlayerSession session = sessionManager.get(player.getUniqueId());

        if (!isValid) {
            sendHelp(sender);
            return;
        }

        AlertType alertType = AlertType.valueOf(alertTypeName.toUpperCase());
        boolean isEnabled = option.isEmpty() ? !session.shouldNotify(alertType) : option.equalsIgnoreCase("enable");

        switch (alertType) {
            case NAME_CHANGE:
                if (permission.has(player, options.permissionNameChange) || !shouldCheckPermission) {
                    session.setAlertOption(alertType, isEnabled);
                    wasChanged = true;
                } else message.send(player, messages.noPermission, messages.prefixGeneral);
                break;
            case MENTION:
                if (permission.has(player, options.permissionMention) || !shouldCheckPermission) {
                    session.setAlertOption(alertType, isEnabled);
                    wasChanged = true;
                } else message.send(player, messages.noPermission, messages.prefixGeneral);
                break;
            case XRAY:
                if (permission.has(player, options.permissionXray) || !shouldCheckPermission) {
                    session.setAlertOption(alertType, isEnabled);
                    wasChanged = true;
                } else message.send(player, messages.noPermission, messages.prefixGeneral);
                break;
            case ALT_DETECT:
                if (permission.has(player, options.permissionAlertsAltDetect) || !shouldCheckPermission) {
                    session.setAlertOption(alertType, isEnabled);
                    wasChanged = true;
                } else message.send(player, messages.noPermission, messages.prefixGeneral);
                break;
        }

        if (wasChanged && shouldCheckPermission) {
            message.send(player, messages.alertChanged.replace("%alerttype%", alertTypeName.replace("_", " ")).replace("%status%", isEnabled ? "enabled" : "disabled"), messages.prefixGeneral);
        }
    }

    private void sendHelp(CommandSender sender) {
        message.send(sender, "&7" + message.LONG_LINE, "");
        message.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixGeneral);
        message.send(sender, "&7" + message.LONG_LINE, "");
    }
}