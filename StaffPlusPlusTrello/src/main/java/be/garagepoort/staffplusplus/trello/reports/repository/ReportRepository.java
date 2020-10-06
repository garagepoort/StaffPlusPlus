package be.garagepoort.staffplusplus.trello.reports.repository;

import be.garagepoort.staffplusplus.trello.reports.Report;

import java.util.Optional;

public interface ReportRepository {

    Optional<Report> findReportBySppId(int sppUuid);

    void createReport(Report report);
}
