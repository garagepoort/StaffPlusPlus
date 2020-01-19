package net.shortninja.staffplus.server;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.AlertType;
import net.shortninja.staffplus.unordered.IReport;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.unordered.IWarning;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

public class AlertCoordinator {
    private static Set<Location> notifiedLocations = new HashSet<Location>();
    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private UserManager userManager = StaffPlus.get().userManager;

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

        for (IUser user : userManager.getAll()) {
            if (!user.getPlayer().isPresent()) { // How?
                continue;
            }

            if (user.shouldNotify(AlertType.NAME_CHANGE) && permission.has(user.getPlayer().get(), options.permissionNameChange)) {
                message.send(user.getPlayer().get(), messages.alertsName.replace("%old%", originalName).replace("%new%", newName), messages.prefixGeneral, options.permissionNameChange);
            }
        }

        fixInfractionNames(originalName, newName);
    }

    public void onMention(IUser user, String mentioner) {
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

        for (IUser user : userManager.getAll()) {
            if (!user.getPlayer().isPresent()) {
                continue; // How?
            }

            if (user.shouldNotify(AlertType.XRAY) && permission.has(user.getPlayer().get(), options.permissionXray)) {
                message.send(user.getPlayer().get(), messages.alertsXray.replace("%target%", miner).replace("%count%", Integer.toString(amount)).replace("%itemtype%", JavaUtils.formatTypeName(type)).replace("%lightlevel%", Integer.toString(lightLevel)), messages.prefixGeneral, options.permissionXray);
            }
        }
    }

    /**
     * This fixes the issue with reports and warnings having incorrect names after
     * a player changes his or her name. Any unhandled fixes will be done on reload.
     */
    private void fixInfractionNames(String originalName, String newName) {
        for (IUser user : userManager.getAll()) {
            for (IReport report : user.getReports()) {
                if (report.getReporterName().equals(originalName)) {
                    report.setReporterName(newName);
                }
            }

            for (IWarning warning : user.getWarnings()) {
                if (warning.getIssuerName().equals(originalName)) {
                    warning.setIssuerName(newName);
                }
            }
        }
    }
}