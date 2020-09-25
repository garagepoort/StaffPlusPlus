package net.shortninja.staffplus.server;

import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.AlertType;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

public class AlertCoordinator {
    private final static Set<Location> notifiedLocations = new HashSet<>();
    private final PermissionHandler permission;
    private final MessageCoordinator message;
    private final Options options;
    private final Messages messages;
    private final SessionManager sessionManager;

    public AlertCoordinator(PermissionHandler permission, MessageCoordinator message, Options options, Messages messages, SessionManager sessionManager) {
        this.permission = permission;
        this.message = message;
        this.options = options;
        this.messages = messages;
        this.sessionManager = sessionManager;
    }

    public boolean hasNotified(Location location) {
        return notifiedLocations.contains(location);
    }

    public int getNotifiedAmount() {
        return notifiedLocations.size();
    }

    public void addNotified(Location location) {
        notifiedLocations.add(location);
    }

    public void clearNotified() {
        notifiedLocations.clear();
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

    public void onMention(PlayerSession user, String mentioner) {
        if (!options.alertsMentionNotify || user == null || !user.getPlayer().isPresent()) {
            return;
        }

        if (user.shouldNotify(AlertType.MENTION) && permission.has(user.getPlayer().get(), options.permissionMention)) {
            message.send(user.getPlayer().get(), messages.alertsMention.replace("%target%", mentioner), messages.prefixGeneral, options.permissionMention);
            options.alertsSound.play(user.getPlayer().get());
        }
    }

    public void onXray(String miner, int amount, Material type, int lightLevel) {
        if (!options.alertsXrayEnabled) {
            return;
        }

        for (PlayerSession user : sessionManager.getAll()) {
            if (!user.getPlayer().isPresent()) {
                continue; // How?
            }

            if (user.shouldNotify(AlertType.XRAY) && permission.has(user.getPlayer().get(), options.permissionXray)) {
                message.send(user.getPlayer().get(), messages.alertsXray.replace("%target%", miner).replace("%count%", Integer.toString(amount)).replace("%itemtype%", JavaUtils.formatTypeName(type)).replace("%lightlevel%", Integer.toString(lightLevel)), messages.prefixGeneral, options.permissionXray);
            }
        }
    }
}