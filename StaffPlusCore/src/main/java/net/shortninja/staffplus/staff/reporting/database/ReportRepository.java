package net.shortninja.staffplus.staff.reporting.database;

import net.shortninja.staffplus.staff.reporting.Report;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ReportRepository {

    int addReport(Report report);

    List<Report> getReports(UUID uuid, int offset, int amount);

    List<Report> getUnresolvedReports(int offset, int amount);

    void removeReports(UUID playerUuid);

    Optional<Report> findOpenReport(int reportId);

    Optional<Report> findReport(int reportId);

    void updateReport(Report report);

    void markReportDeleted(Report report);

    List<Report> getAssignedReports(UUID staffUuid, int offset, int amount);

    List<Report> getMyReports(UUID staffUuid, int offset, int amount);

    List<Report> getMyReports(UUID reporterUuid);

    List<Report> getClosedReports(int offset, int amount);

    List<Report> getReportsByOffender(UUID playerUUID);

    Map<UUID, Integer> getReportedCount();
}
