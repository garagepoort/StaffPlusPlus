package net.shortninja.staffplus.reporting.database;

import net.shortninja.staffplus.reporting.Report;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportRepository {

    void addReport(Report report);

    List<Report> getReports(UUID uuid);

    List<Report> getUnresolvedReports();

    List<Report> getUnresolvedReports(UUID playerUuid);

    void removeReports(UUID playerUuid);

    Optional<Report> findOpenReport(int reportId);

    Optional<Report> findReport(int reportId);

    void updateReport(Report report);

    List<Report> getAssignedReports(UUID staffUuid);

    List<Report> getClosedReports();
}
