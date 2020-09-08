package net.shortninja.staffplus.player.attribute.infraction;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.reporting.ReportService;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.unordered.IWarning;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.warn.WarnService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class InfractionCoordinator {
    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private UserManager userManager = StaffPlus.get().userManager;
    private ReportService reportService = ReportService.getInstance();
    private WarnService warnService = WarnService.getInstance();

    public Set<IWarning> getWarnings() {
        Set<IWarning> warnings = new HashSet<>();

        for (IUser user : userManager.getAll()) {
            warnings.addAll(user.getWarnings());
        }

        return warnings;
    }

    public void sendReport(CommandSender sender, String playerName, String reason) {
        IUser user = userManager.getOnOrOfflineUser(playerName);
        if (user == null) {
            message.send(sender, messages.playerOffline, messages.prefixGeneral);
            return;
        }

        String reporterName = sender instanceof Player ? sender.getName() : "Console";
        UUID reporterUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : StaffPlus.get().consoleUUID;
        Report report = new Report(user.getUuid(), user.getName(), reason, reporterName, reporterUuid);

        // Offline users cannot bypass being reported this way. Permissions are taken away upon logging out
        if (user.isOnline() && permission.has(user.getPlayer().get(), options.permissionReportBypass)) {
            message.send(sender, messages.bypassed, messages.prefixGeneral);
            return;
        }

        reportService.addReport(user, report);
        message.send(sender, messages.reported.replace("%player%", report.getReporterName()).replace("%target%", report.getName()).replace("%reason%", report.getReason()), messages.prefixReports);
        message.sendGroupMessage(messages.reportedStaff.replace("%target%", report.getReporterName()).replace("%player%", report.getName()).replace("%reason%", report.getReason()), options.permissionReport, messages.prefixReports);
        options.reportsSound.playForGroup(options.permissionReport);
    }

    public void sendWarning(CommandSender sender, String playerName, String reason) {
        IUser user = userManager.getOnOrOfflineUser(playerName);
        if (user == null) {
            message.send(sender, messages.playerOffline, messages.prefixGeneral);
            return;
        }

        String issuerName = sender instanceof Player ? sender.getName() : "Console";
        UUID issuerUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : StaffPlus.get().consoleUUID;
        Warning warning = new Warning(user.getUuid(), playerName, reason, issuerName, issuerUuid, System.currentTimeMillis());


        // Offline users cannot bypass being warned this way. Permissions are taken away upon logging out
        if (user.isOnline() && permission.has(user.getPlayer().get(), options.permissionWarnBypass)) {
            message.send(sender, messages.bypassed, messages.prefixGeneral);
            return;
        }

        warnService.addWarn(user, warning);
        message.send(sender, messages.warned.replace("%target%", warning.getName()).replace("%reason%", warning.getReason()), messages.prefixWarnings);

        if(user.isOnline()) {
            Optional<Player> p = user.getPlayer();
            message.send(p.get(), messages.warn.replace("%reason%", warning.getReason()), messages.prefixWarnings);
            options.warningsSound.play(p.get());
            warnService.checkBan(user);
        }
    }
}