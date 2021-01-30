package net.shortninja.staffplus.staff.reporting;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.event.CreateReportEvent;
import net.shortninja.staffplus.event.ReportStatus;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.infractions.Infraction;
import net.shortninja.staffplus.staff.infractions.InfractionCount;
import net.shortninja.staffplus.staff.infractions.InfractionProvider;
import net.shortninja.staffplus.staff.reporting.database.ReportRepository;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.time.ZonedDateTime;
import java.util.*;

import static org.bukkit.Bukkit.getScheduler;

public class ReportService implements InfractionProvider {

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

            int id = reportRepository.addReport(report);
            report.setId(id);

            message.send(sender, messages.reported.replace("%player%", report.getReporterName()).replace("%target%", report.getCulpritName()).replace("%reason%", report.getReason()), messages.prefixReports);
            message.sendGroupMessage(messages.reportedStaff.replace("%target%", report.getReporterName()).replace("%player%", report.getCulpritName()).replace("%reason%", report.getReason()), options.permissionReportUpdateNotifications, messages.prefixReports);
            options.reportConfiguration.getSound().playForGroup(options.permissionReportUpdateNotifications);

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

            int id = reportRepository.addReport(report);
            report.setId(id);

            message.send(sender, messages.reported.replace("%player%", report.getReporterName()).replace("%target%", "unknown").replace("%reason%", report.getReason()), messages.prefixReports);
            message.sendGroupMessage(messages.reportedStaff.replace("%target%", report.getReporterName()).replace("%player%", "unknown").replace("%reason%", report.getReason()), options.permissionReportUpdateNotifications, messages.prefixReports);
            options.reportConfiguration.getSound().playForGroup(options.permissionReportUpdateNotifications);

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

    public Collection<Report> getMyReports(UUID reporterUuid, int offset, int amount) {
        return reportRepository.getMyReports(reporterUuid, offset, amount);
    }

    public List<Report> getMyReports(UUID reporterUuid) {
        return reportRepository.getMyReports(reporterUuid);
    }

    private void validateCoolDown(CommandSender sender) {
        long last = sender instanceof Player ? (lastUse.containsKey(((Player) sender).getUniqueId()) ? lastUse.get(((Player) sender).getUniqueId()) : 0) : 0;
        long remaining = (System.currentTimeMillis() - last) / 1000;

        if (remaining < options.reportConfiguration.getCooldown()) {
            throw new BusinessException(messages.commandOnCooldown.replace("%seconds%", Long.toString(options.reportConfiguration.getCooldown() - remaining)), messages.prefixGeneral);
        }
    }

    private SppPlayer getUser(UUID playerUuid) {
        Optional<SppPlayer> player = playerManager.getOnOrOfflinePlayer(playerUuid);
        if (!player.isPresent()) {
            throw new BusinessException(messages.playerNotRegistered, messages.prefixGeneral);
        }
        return player.get();
    }

    public Report getReport(int reportId) {
        return reportRepository.findReport(reportId)
            .orElseThrow(() -> new BusinessException("Report with id [" + reportId + "] not found", messages.prefixReports));
    }

    private void sendEvent(Event event) {
        getScheduler().runTask(StaffPlus.get(), () -> {
            Bukkit.getPluginManager().callEvent(event);
        });
    }

    @Override
    public List<? extends Infraction> getInfractions(Player executor, UUID playerUUID) {
        if(!options.infractionsConfiguration.isShowReported()) {
            return Collections.emptyList();
        }
        return reportRepository.getReportsByOffender(playerUUID);
    }

    @Override
    public Optional<InfractionCount> getInfractionsCount() {
        if (!options.infractionsConfiguration.isShowReported()) {
            return Optional.empty();
        }
        return Optional.of(new InfractionCount("Reported", reportRepository.getReportedCount()));
    }
}
