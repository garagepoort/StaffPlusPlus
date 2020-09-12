package net.shortninja.staffplus.reporting.database;

import net.shortninja.staffplus.event.ReportStatus;
import net.shortninja.staffplus.reporting.Report;

import java.util.*;
import java.util.stream.Collectors;

public final class InMemoryReportRepository implements ReportRepository {

    private final Map<UUID, DataHolder> data = new HashMap<>();

    @Override
    public List<Report> getReports(UUID uuid) {
        return getOrPut(uuid).reports;
    }

    @Override
    public List<Report> getUnresolvedReports() {
        return data.values().stream()
                .flatMap(dataHolder -> dataHolder.reports.stream())
                .filter(r -> r.getReportStatus() == ReportStatus.OPEN)
                .collect(Collectors.toList());
    }

    @Override
    public List<Report> getUnresolvedReports(UUID playerUuid) {
        return getOrPut(playerUuid).reports.stream()
                .filter(r -> r.getReportStatus() == ReportStatus.OPEN)
                .collect(Collectors.toList());
    }

    @Override
    public void addReport(Report report) {
        getOrPut(report.getCulpritUuid()).reports.add(report);
    }

    @Override
    public void removeReports(UUID userUuid) {
        getOrPut(userUuid).reports.removeIf(r -> r.getCulpritUuid().equals(userUuid));
    }

    @Override
    public Optional<Report> findOpenReport(int reportId) {
        return getUnresolvedReports().stream().filter(r -> r.getId() == reportId).findFirst();
    }

    private DataHolder getOrPut(UUID id) {
        if (data.containsKey(id)) {
            return data.get(id);
        } else {
            final DataHolder holder = new DataHolder();
            data.putIfAbsent(id, holder);

            return holder;
        }
    }

    private static class DataHolder {
        private final List<Report> reports = new ArrayList<>();
    }
}
