package net.shortninja.staffplus.staff.alerts;

import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManagerImpl;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.common.JavaUtils;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.altdetect.IAltDetectResult;
import org.bukkit.Material;

public class AlertCoordinator {
    private final PermissionHandler permission;
    private final MessageCoordinator message;
    private final Options options;
    private final Messages messages;
    private final SessionManagerImpl sessionManager;

    public AlertCoordinator(PermissionHandler permission, MessageCoordinator message, Options options, Messages messages, SessionManagerImpl sessionManager) {
        this.permission = permission;
        this.message = message;
        this.options = options;
        this.messages = messages;
        this.sessionManager = sessionManager;
    }

    public void onNameChange(String originalName, String newName) {
        if (!options.alertsNameNotify) {
            return;
        }

        for (PlayerSession playerSession : sessionManager.getAll()) {
            if (!playerSession.getPlayer().isPresent()) { // How?
                continue;
            }

            if (playerSession.shouldNotify(AlertType.NAME_CHANGE) && permission.has(playerSession.getPlayer().get(), options.permissionNameChange)) {
                message.send(playerSession.getPlayer().get(), messages.alertsName.replace("%old%", originalName).replace("%new%", newName), messages.prefixGeneral, options.permissionNameChange);
            }
        }
    }

    public void onAltDetect(IAltDetectResult altDetectResult) {
        if (!options.alertsAltDetectEnabled || !options.alertsAltDetectTrustLevels.contains(altDetectResult.getAltDetectTrustLevel())) {
            return;
        }

        for (PlayerSession playerSession : sessionManager.getAll()) {
            if (playerSession.getPlayer().isPresent()) {
                if (playerSession.shouldNotify(AlertType.ALT_DETECT) && permission.has(playerSession.getPlayer().get(), options.permissionAlertsAltDetect)) {
                    message.send(playerSession.getPlayer().get(), String.format("&CAlt account check triggered, %s and %s might be the same player. Trust [%s]",
                        altDetectResult.getPlayerCheckedName(),
                        altDetectResult.getPlayerMatchedName(),
                        altDetectResult.getAltDetectTrustLevel()), messages.prefixGeneral);
                }
            }
        }
    }

    public void onMention(PlayerSession user, String mentioner) {
        if (!options.alertsMentionNotify || user == null || !user.getPlayer().isPresent()) {
            return;
        }

        if (user.shouldNotify(AlertType.MENTION) && permission.has(user.getPlayer().get(), options.permissionMention)) {
            message.send(user.getPlayer().get(), messages.alertsMention.replace("%target%", mentioner), messages.prefixGeneral, options.permissionMention);
            options.alertsSound.play(user.getPlayer().get());
        }
    }

    public void onXray(String miner, int amount, Long duration, Material type, int lightLevel) {
        if (!options.alertsXrayEnabled) {
            return;
        }

        for (PlayerSession user : sessionManager.getAll()) {
            if (!user.getPlayer().isPresent()) {
                continue; // How?
            }

            if (user.shouldNotify(AlertType.XRAY) && permission.has(user.getPlayer().get(), options.permissionXray)) {
                String xrayMessage = messages.alertsXray.replace("%target%", miner).replace("%count%", Integer.toString(amount)).replace("%itemtype%", JavaUtils.formatTypeName(type)).replace("%lightlevel%", Integer.toString(lightLevel));
                if (duration != null) {
                    xrayMessage = xrayMessage + String.format(" in %s seconds", duration/1000);
                }
                message.send(user.getPlayer().get(), xrayMessage, messages.prefixGeneral, options.permissionXray);
            }
        }
    }
}