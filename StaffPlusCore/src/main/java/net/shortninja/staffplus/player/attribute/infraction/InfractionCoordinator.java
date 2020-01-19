package net.shortninja.staffplus.player.attribute.infraction;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.event.ReportPlayerEvent;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.unordered.IWarning;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class InfractionCoordinator {
    private static Map<UUID, Report> unresolvedReports = new HashMap<UUID, Report>();
    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private UserManager userManager = StaffPlus.get().userManager;

    public Collection<Report> getUnresolvedReports() {
        return unresolvedReports.values();
    }

    public Report getUnresolvedReport(UUID uuid) {
        return unresolvedReports.get(uuid);
    }

    public void addUnresolvedReport(Report report) {
        unresolvedReports.put(report.getUuid(), report);
        Bukkit.getPluginManager().callEvent(new ReportPlayerEvent(report));
    }

    public void removeUnresolvedReport(UUID uuid) {
        unresolvedReports.remove(uuid);
    }

    public Set<IWarning> getWarnings() {
        Set<IWarning> warnings = new HashSet<>();

        for (IUser user : userManager.getAll()) {
            warnings.addAll(user.getWarnings());
        }

        return warnings;
    }

    public void sendReport(CommandSender sender, Report report) {
        IUser user = userManager.get(report.getUuid());

        if (user == null || !user.getPlayer().isPresent()) {
            message.send(sender, messages.playerOffline, messages.prefixGeneral);
            return;
        }

        if (permission.has(user.getPlayer().get(), options.permissionReportBypass)) {
            message.send(sender, messages.bypassed, messages.prefixGeneral);
            return;
        }

        addUnresolvedReport(report);
        user.addReport(report);
        message.send(sender, messages.reported.replace("%player%", report.getReporterName()).replace("%target%", report.getName()).replace("%reason%", report.getReason()), messages.prefixReports);
        message.sendGroupMessage(messages.reportedStaff.replace("%target%", report.getReporterName()).replace("%player%", report.getName()).replace("%reason%", report.getReason()), options.permissionReport, messages.prefixReports);
        options.reportsSound.playForGroup(options.permissionReport);
    }

    public void clearReports(IUser user) {
        user.getReports().clear();

        if (unresolvedReports.containsKey(user.getUuid())) {
            unresolvedReports.remove(user.getUuid());
        }
    }

    public void sendWarning(CommandSender sender, IWarning warning) {
        IUser user = userManager.get(warning.getUuid());
        Optional<Player> p = user.getPlayer();

        if (!p.isPresent()) {
            message.send(sender, messages.playerOffline, messages.prefixGeneral);
            return;
        }

        if (permission.has(user.getPlayer().get(), options.permissionWarnBypass)) {
            message.send(sender, messages.bypassed, messages.prefixGeneral);
            return;
        }

        user.addWarning(warning);
        message.send(sender, messages.warned.replace("%target%", warning.getName()).replace("%reason%", warning.getReason()), messages.prefixWarnings);
        message.send(p.get(), messages.warn.replace("%reason%", warning.getReason()), messages.prefixWarnings);
        options.warningsSound.play(p.get());

        if (user.getWarnings().size() >= options.warningsMaximum && options.warningsMaximum > 0) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), options.warningsBanCommand.replace("%player%", p.get().getName()));
        }
    }
}