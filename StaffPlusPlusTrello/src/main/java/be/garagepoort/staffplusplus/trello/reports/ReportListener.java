package be.garagepoort.staffplusplus.trello.reports;

import be.garagepoort.staffplusplus.trello.Constants;
import be.garagepoort.staffplusplus.trello.StaffPlusPlusTrello;
import be.garagepoort.staffplusplus.trello.api.TrelloCardRequest;
import be.garagepoort.staffplusplus.trello.api.TrelloCardResponse;
import be.garagepoort.staffplusplus.trello.api.TrelloClient;
import be.garagepoort.staffplusplus.trello.api.TrelloListResponse;
import be.garagepoort.staffplusplus.trello.reports.repository.ReportRepository;
import be.garagepoort.staffplusplus.trello.reports.repository.SqliteReportRepository;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import net.shortninja.staffplusplus.reports.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Optional;

public class ReportListener implements Listener {

    private TrelloClient trelloClient;
    private FileConfiguration config;
    private ReportTrelloConfiguration trelloConfig;
    private TrelloListResponse rejectedList;
    private TrelloListResponse openList;
    private TrelloListResponse acceptedList;
    private TrelloListResponse resolvedList;
    private ReportRepository reportRepository = new SqliteReportRepository();

    public ReportListener(FileConfiguration config) {
        this.config = config;
    }

    public void init() {
        trelloClient = Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .logger(new Slf4jLogger(TrelloClient.class))
            .logLevel(Logger.Level.FULL)
            .requestInterceptor(new TrelloReportsRequestInterceptor(config))
            .target(TrelloClient.class, Constants.TRELLO_URL);

        checkConfig();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleCreateReport(CreateReportEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlusPlusTrello.get(), () -> {
            IReport report = event.getReport();
            TrelloCardResponse card = trelloClient.createCard(new TrelloCardRequest(getTitle(report), openList.getId(), getDescription(report, false)));
            reportRepository.createReport(new Report(report.getId(), card.getId()));
        });
    }

    private String getTitle(IReport report) {
        if (report.getReason().length() <= 30) {
            return "Staff++ Report: " + report.getReason();
        }
        return "Staff++ Report: " + report.getReason().substring(0, 27) + "...";
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleReopenReport(ReopenReportEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlusPlusTrello.get(), () -> {
            IReport report = event.getReport();
            Optional<Report> reportBySppUuid = reportRepository.findReportBySppId(report.getId());
            if (reportBySppUuid.isPresent()) {
                trelloClient.updateCard(reportBySppUuid.get().getTrelloId(), new TrelloCardRequest(getTitle(report), openList.getId(), getDescription(report, false)));
            }
        });
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void handleAcceptReport(AcceptReportEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlusPlusTrello.get(), () -> {
            IReport report = event.getReport();
            Optional<Report> reportBySppUuid = reportRepository.findReportBySppId(report.getId());
            if (reportBySppUuid.isPresent()) {
                trelloClient.updateCard(reportBySppUuid.get().getTrelloId(), new TrelloCardRequest(getTitle(report), acceptedList.getId(), getDescription(report, true)));
            }
        });
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void handleRejectReport(RejectReportEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlusPlusTrello.get(), () -> {
            IReport report = event.getReport();
            Optional<Report> reportBySppUuid = reportRepository.findReportBySppId(report.getId());
            if (reportBySppUuid.isPresent()) {
                trelloClient.updateCard(reportBySppUuid.get().getTrelloId(), new TrelloCardRequest(getTitle(report), rejectedList.getId(), getDescription(report, true)));
            }
        });
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleResolveReport(ResolveReportEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlusPlusTrello.get(), () -> {
            IReport report = event.getReport();
            Optional<Report> reportBySppUuid = reportRepository.findReportBySppId(report.getId());
            if (reportBySppUuid.isPresent()) {
                trelloClient.updateCard(reportBySppUuid.get().getTrelloId(), new TrelloCardRequest(getTitle(report), resolvedList.getId(), getDescription(report, true)));
            }
        });
    }

    private void checkConfig() {
        trelloConfig = new ReportTrelloConfiguration(config);
        List<TrelloListResponse> listsOfBoard = trelloClient.getListsOfBoard(trelloConfig.getBoardId());
        resolvedList = getList(listsOfBoard, trelloConfig.getResolvedListName());
        acceptedList = getList(listsOfBoard, trelloConfig.getAcceptedListName());
        rejectedList = getList(listsOfBoard, trelloConfig.getRejectedListName());
        openList = getList(listsOfBoard, trelloConfig.getOpenListName());

    }

    private String getDescription(IReport report, boolean showStaff) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("### Reporter\n");
        stringBuilder.append(String.format("%s [%s]\n", report.getReporterName(), report.getReporterUuid()));
        stringBuilder.append("### Culprit\n");
        if (report.getCulpritUuid() != null) {
            stringBuilder.append(String.format("%s [%s]\n", report.getCulpritName(), report.getCulpritUuid()));
        } else {
            stringBuilder.append("Unknown\n");
        }
        stringBuilder.append("### Reason\n");
        stringBuilder.append(report.getReason() + "\n");
        if (showStaff) {
            stringBuilder.append("### Staff assigned\n");
            stringBuilder.append(String.format("%s [%s]\n", report.getStaffName(), report.getStaffUuid()));
        }

        stringBuilder.append("### Status\n");
        stringBuilder.append(report.getReportStatus() + "\n");
        if (report.getReportStatus() == ReportStatus.REJECTED || report.getReportStatus() == ReportStatus.RESOLVED) {
            stringBuilder.append("### Reason for closing\n");
            stringBuilder.append(report.getCloseReason() + "\n");
        }
        return stringBuilder.toString();
    }

    private TrelloListResponse getList(List<TrelloListResponse> listsOfBoard, String resolvedListName) {
        return listsOfBoard.stream()
            .filter(l -> l.getName().equals(resolvedListName))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No list could be found with the name [" + resolvedListName + "] on the configured board"));
    }

}