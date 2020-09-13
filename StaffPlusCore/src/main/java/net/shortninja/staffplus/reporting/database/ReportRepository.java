package net.shortninja.staffplus.reporting.database;

import net.shortninja.staffplus.reporting.Report;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportRepository {

    void addReport(Report report);

    List<Report> getReports(UUID uuid, int offset, int amount);

    List<Report> getUnresolvedReports(int offset, int amount);

    List<Report> getUnresolvedReports(UUID playerUuid, int offset, int amount);

    void removeReports(UUID playerUuid);

    Optional<Report> findOpenReport(int reportId);

    Optional<Report> findReport(int reportId);

    void updateReport(Report report);

    List<Report> getAssignedReports(UUID staffUuid, int offset, int amount);

    List<Report> getClosedReports(int offset, int amount);
}
