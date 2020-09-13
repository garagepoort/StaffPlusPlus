package net.shortninja.staffplus.reporting;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.BusinessException;
import net.shortninja.staffplus.event.ReportPlayerEvent;
import net.shortninja.staffplus.event.ReportStatus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.reporting.database.ReportRepository;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class ReportPlayerService {

    private static Map<UUID, Long> lastUse = new HashMap<UUID, Long>();

    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages;
    private UserManager userManager;
    private ReportRepository reportRepository;

    public ReportPlayerService(ReportRepository reportRepository, UserManager userManager, Messages messages) {
        this.reportRepository = reportRepository;
        this.userManager = userManager;
        this.messages = messages;
    }

    public List<Report> getReports(String playerName, int offset, int amount) {
        IUser user = getUser(playerName);
        return reportRepository.getReports(user.getUuid(), offset, amount);
    }

    public List<Report> getReports(UUID playerUuid, int offset, int amount) {
        IUser user = getUser(playerUuid);
        return reportRepository.getReports(user.getUuid(), offset, amount);
    }

    public void sendReport(CommandSender sender, String playerName, String reason) {
        validateCoolDown(sender);
        IUser user = getUser(playerName);

        // Offline users cannot bypass being reported this way. Permissions are taken away upon logging out
        if (user.isOnline() && permission.has(user.getPlayer().get(), options.permissionReportBypass)) {
            message.send(sender, messages.bypassed, messages.prefixGeneral);
            return;
        }

        String reporterName = sender instanceof Player ? sender.getName() : "Console";
        UUID reporterUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : StaffPlus.get().consoleUUID;
        Report report = new Report(
                user.getUuid(),
                user.getName(),
                reason,
                reporterName,
                reporterUuid,
                ReportStatus.OPEN);

        reportRepository.addReport(report);

        message.send(sender, messages.reported.replace("%player%", report.getReporterName()).replace("%target%", report.getCulpritName()).replace("%reason%", report.getReason()), messages.prefixReports);
        message.sendGroupMessage(messages.reportedStaff.replace("%target%", report.getReporterName()).replace("%player%", report.getCulpritName()).replace("%reason%", report.getReason()), options.permissionReport, messages.prefixReports);
        options.reportsSound.playForGroup(options.permissionReport);

        if (sender instanceof Player) {
            lastUse.put(reporterUuid, System.currentTimeMillis());
        }
        Bukkit.getPluginManager().callEvent(new ReportPlayerEvent(report));
    }

    public void sendReport(CommandSender sender, String reason) {
        validateCoolDown(sender);

        String reporterName = sender instanceof Player ? sender.getName() : "Console";
        UUID reporterUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : StaffPlus.get().consoleUUID;
        Report report = new Report(
                null,
                null,
                reason,
                reporterName,
                reporterUuid,
                ReportStatus.OPEN);

        reportRepository.addReport(report);

        message.send(sender, messages.reported.replace("%player%", report.getReporterName()).replace("%target%", "unknown").replace("%reason%", report.getReason()), messages.prefixReports);
        message.sendGroupMessage(messages.reportedStaff.replace("%target%", report.getReporterName()).replace("%player%", "unknown").replace("%reason%", report.getReason()), options.permissionReport, messages.prefixReports);
        options.reportsSound.playForGroup(options.permissionReport);

        if (sender instanceof Player) {
            lastUse.put(reporterUuid, System.currentTimeMillis());
        }
        Bukkit.getPluginManager().callEvent(new ReportPlayerEvent(report));
    }

    public Collection<Report> getUnresolvedReports(int offset, int amount) {
        return reportRepository.getUnresolvedReports(offset, amount);
    }

    public Collection<Report> getAssignedReports(UUID staffUuid, int offset, int amount) {
        return reportRepository.getAssignedReports(staffUuid, offset, amount);
    }

    public Collection<Report> getUnresolvedReports(UUID playerUuid, int offset, int amount) {
        return reportRepository.getUnresolvedReports(playerUuid, offset, amount);
    }

    public void clearReports(String playerName) {
        IUser user = getUser(playerName);
        reportRepository.removeReports(user.getUuid());
    }

    private void validateCoolDown(CommandSender sender) {
        long last = sender instanceof Player ? (lastUse.containsKey(((Player) sender).getUniqueId()) ? lastUse.get(((Player) sender).getUniqueId()) : 0) : 0;
        long remaining = (System.currentTimeMillis() - last) / 1000;

        if (remaining < options.reportsCooldown && !permission.has(sender, options.permissionReport)) {
            throw new BusinessException(messages.commandOnCooldown.replace("%seconds%", Long.toString(options.reportsCooldown - remaining)), messages.prefixGeneral);
        }
    }

    private IUser getUser(String playerName) {
        IUser user = userManager.getOnOrOfflineUser(playerName);
        if (user == null) {
            throw new BusinessException(messages.playerNotRegistered, messages.prefixGeneral);
        }
        return user;
    }

    private IUser getUser(UUID playerUuid) {
        IUser user = userManager.getOnOrOfflineUser(playerUuid);
        if (user == null) {
            throw new BusinessException(messages.playerNotRegistered, messages.prefixGeneral);
        }
        return user;
    }

    public void acceptReport(Player player, int reportId) {
        Report report = reportRepository.findOpenReport(reportId)
                .orElseThrow(() -> new BusinessException("Report with id [" + reportId + "] not found", messages.prefixReports));

        report.setReportStatus(ReportStatus.IN_PROGRESS);
        report.setStaffUuid(player.getUniqueId());
        report.setStaffName(player.getName());
        reportRepository.updateReport(report);
        message.sendGroupMessage(player.getName() + " accepted report from " + report.getReporterName(), options.permissionReport, messages.prefixReports);

    }

    public Report getReport(int reportId) {
        return reportRepository.findReport(reportId)
                .orElseThrow(() -> new BusinessException("Report with id [" + reportId + "] not found", messages.prefixReports));
    }

    public void resolveReport(Player player, int reportId) {
        Report report = getReport(reportId);
        changeStatus(player, report, ReportStatus.RESOLVED);
        message.sendGroupMessage(player.getName() + " resolved report from " + report.getReporterName(), options.permissionReport, messages.prefixReports);
    }

    public void reopenReport(Player player, int reportId) {
        Report report = getReport(reportId);
        changeStatus(player, report, ReportStatus.OPEN);
        message.sendGroupMessage(player.getName() + " reopened report from " + report.getReporterName(), options.permissionReport, messages.prefixReports);
    }

    public void rejectReport(Player player, int reportId) {
        Report report = getReport(reportId);
        changeStatus(player, report, ReportStatus.REJECTED);
        message.sendGroupMessage(player.getName() + " closed report from " + report.getReporterName(), options.permissionReport, messages.prefixReports);
    }

    private void changeStatus(Player player, Report report, ReportStatus rejected) {
        if (!report.getStaffUuid().equals(player.getUniqueId())) {
            throw new BusinessException("You cannot change the status of a report you are not assigned to", messages.prefixReports);
        }

        report.setReportStatus(rejected);
        reportRepository.updateReport(report);
    }

    public List<Report> getClosedReports(int offset, int amount) {
        return reportRepository.getClosedReports(offset, amount);
    }
}
