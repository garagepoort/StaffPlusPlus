package net.shortninja.staffplus.reporting;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.BusinessException;
import net.shortninja.staffplus.event.*;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.reporting.database.ReportRepository;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.time.ZonedDateTime;
import java.util.*;

import static org.bukkit.Bukkit.getScheduler;

public class ReportService {

    private static final Map<UUID, Long> lastUse = new HashMap<UUID, Long>();

    private final PermissionHandler permission = IocContainer.getPermissionHandler();
    private final MessageCoordinator message = IocContainer.getMessage();
    private final Options options = IocContainer.getOptions();
    private final Messages messages;
    private final PlayerManager playerManager;
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository, Messages messages, PlayerManager playerManager) {
        this.reportRepository = reportRepository;
        this.messages = messages;
        this.playerManager = playerManager;
    }

    public List<Report> getReports(SppPlayer player, int offset, int amount) {
        return reportRepository.getReports(player.getId(), offset, amount);
    }

    public List<Report> getReports(UUID playerUuid, int offset, int amount) {
        SppPlayer user = getUser(playerUuid);
        return reportRepository.getReports(user.getId(), offset, amount);
    }

    public void sendReport(CommandSender sender, SppPlayer user, String reason) {
        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            validateCoolDown(sender);

            // Offline users cannot bypass being reported this way. Permissions are taken away upon logging out
            if (user.isOnline() && permission.has(user.getPlayer(), options.permissionReportBypass)) {
                message.send(sender, messages.bypassed, messages.prefixGeneral);
                return;
            }

            String reporterName = sender instanceof Player ? sender.getName() : "Console";
            UUID reporterUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : StaffPlus.get().consoleUUID;
            Report report = new Report(
                    user.getId(),
                    user.getUsername(),
                    reason,
                    reporterName,
                    reporterUuid,
                    ReportStatus.OPEN,
                    ZonedDateTime.now());

            reportRepository.addReport(report);

            message.send(sender, messages.reported.replace("%player%", report.getReporterName()).replace("%target%", report.getCulpritName()).replace("%reason%", report.getReason()), messages.prefixReports);
            message.sendGroupMessage(messages.reportedStaff.replace("%target%", report.getReporterName()).replace("%player%", report.getCulpritName()).replace("%reason%", report.getReason()), options.permissionReport, messages.prefixReports);
            options.reportsSound.playForGroup(options.permissionReport);

            if (sender instanceof Player) {
                lastUse.put(reporterUuid, System.currentTimeMillis());
            }
            sendEvent(new CreateReportEvent(report));
        });
    }

    public void sendReport(CommandSender sender, String reason) {
        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            validateCoolDown(sender);

            String reporterName = sender instanceof Player ? sender.getName() : "Console";
            UUID reporterUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : StaffPlus.get().consoleUUID;
            Report report = new Report(
                    null,
                    null,
                    reason,
                    reporterName,
                    reporterUuid,
                    ReportStatus.OPEN,
                    ZonedDateTime.now());

            reportRepository.addReport(report);

            message.send(sender, messages.reported.replace("%player%", report.getReporterName()).replace("%target%", "unknown").replace("%reason%", report.getReason()), messages.prefixReports);
            message.sendGroupMessage(messages.reportedStaff.replace("%target%", report.getReporterName()).replace("%player%", "unknown").replace("%reason%", report.getReason()), options.permissionReport, messages.prefixReports);
            options.reportsSound.playForGroup(options.permissionReport);

            if (sender instanceof Player) {
                lastUse.put(reporterUuid, System.currentTimeMillis());
            }
            sendEvent(new CreateReportEvent(report));
        });
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

    public void clearReports(SppPlayer player) {
        reportRepository.removeReports(player.getId());
    }

    private void validateCoolDown(CommandSender sender) {
        long last = sender instanceof Player ? (lastUse.containsKey(((Player) sender).getUniqueId()) ? lastUse.get(((Player) sender).getUniqueId()) : 0) : 0;
        long remaining = (System.currentTimeMillis() - last) / 1000;

        if (remaining < options.reportsCooldown && !permission.has(sender, options.permissionReport)) {
            throw new BusinessException(messages.commandOnCooldown.replace("%seconds%", Long.toString(options.reportsCooldown - remaining)), messages.prefixGeneral);
        }
    }

    private SppPlayer getUser(String playerName) {
        Optional<SppPlayer> player = playerManager.getOnOrOfflinePlayer(playerName);
        if (!player.isPresent()) {
            throw new BusinessException(messages.playerNotRegistered, messages.prefixGeneral);
        }
        return player.get();
    }

    private SppPlayer getUser(UUID playerUuid) {
        Optional<SppPlayer> player = playerManager.getOnOrOfflinePlayer(playerUuid);
        if (!player.isPresent()) {
            throw new BusinessException(messages.playerNotRegistered, messages.prefixGeneral);
        }
        return player.get();
    }

    public void acceptReport(Player player, int reportId) {
        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            Report report = reportRepository.findOpenReport(reportId)
                    .orElseThrow(() -> new BusinessException("Report with id [" + reportId + "] not found", messages.prefixReports));

            report.setReportStatus(ReportStatus.IN_PROGRESS);
            report.setStaffUuid(player.getUniqueId());
            report.setStaffName(player.getName());
            reportRepository.updateReport(report);
            message.sendGroupMessage(player.getName() + " accepted report from " + report.getReporterName(), options.permissionReport, messages.prefixReports);
            sendEvent(new AcceptReportEvent(report));
        });

    }

    public Report getReport(int reportId) {
        return reportRepository.findReport(reportId)
                .orElseThrow(() -> new BusinessException("Report with id [" + reportId + "] not found", messages.prefixReports));
    }

    public void resolveReport(Player player, int reportId) {
        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            Report report = getReport(reportId);
            changeStatus(player, report, ReportStatus.RESOLVED);
            message.sendGroupMessage(player.getName() + " resolved report from " + report.getReporterName(), options.permissionReport, messages.prefixReports);
            sendEvent(new ResolveReportEvent(report));
        });
    }

    public void reopenReport(Player player, int reportId) {
        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            Report report = getReport(reportId);
            if (!report.getStaffUuid().equals(player.getUniqueId())) {
                throw new BusinessException("You cannot change the status of a report you are not assigned to", messages.prefixReports);
            }

            report.setStaffUuid(null);
            report.setStaffName(null);
            report.setReportStatus(ReportStatus.OPEN);
            reportRepository.updateReport(report);
            message.sendGroupMessage(player.getName() + " reopened report from " + report.getReporterName(), options.permissionReport, messages.prefixReports);
            sendEvent(new ReopenReportEvent(report));
        });
    }

    public void rejectReport(Player player, int reportId) {
        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            Report report = getReport(reportId);
            changeStatus(player, report, ReportStatus.REJECTED);
            message.sendGroupMessage(player.getName() + " closed report from " + report.getReporterName(), options.permissionReport, messages.prefixReports);
            sendEvent(new RejectReportEvent(report));
        });
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

    private void sendEvent(Event event) {
        getScheduler().runTask(StaffPlus.get(), () -> {
            Bukkit.getPluginManager().callEvent(event);
        });
    }
}
