package be.garagepoort.staffplusplus.discord;

import be.garagepoort.staffplusplus.discord.api.*;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import net.shortninja.staffplus.event.*;
import net.shortninja.staffplus.unordered.IReport;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReportListener implements Listener {

    private static final String OPEN_COLOR = "6431896";
    private static final String ACCEPTED_COLOR = "16620323";
    private static final String REJECTED_COLOR = "16601379";
    private static final String RESOLVED_COLOR = "5027875";
    private final DiscordClient discordClient;

    public ReportListener() {
        discordClient = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(DiscordClient.class))
                .logLevel(Logger.Level.FULL)
                .target(DiscordClient.class, "https://discordapp.com/api/webhooks/754960272472408114/T7-uhFNukfoCo8UfDxQoWSNP1lUU04T4oeQMyaJv2H_0cediXMr0XIwXTORAQS_5ikqe");

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleCreateReport(CreateReportEvent event) {
        IReport report = event.getReport();

        buildReportMessage(report, "Report created by: " + report.getReporterName(), OPEN_COLOR, false);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleReopenReport(ReopenReportEvent event) {
        IReport report = event.getReport();

        buildReportMessage(report, "Report reopened", OPEN_COLOR, false);
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void handleAcceptReport(AcceptReportEvent event) {
        IReport report = event.getReport();

        buildReportMessage(report, "Report accepted by: " + report.getStaffName(), ACCEPTED_COLOR, true);
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void handleRejectReport(RejectReportEvent event) {
        IReport report = event.getReport();

        buildReportMessage(report, "Report rejected by: " + report.getStaffName(), REJECTED_COLOR, true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleResolveReport(ResolveReportEvent event) {
        IReport report = event.getReport();

        buildReportMessage(report, "Report resolved by: " + report.getStaffName(), RESOLVED_COLOR, true);
    }

    private void buildReportMessage(IReport report, String title, String color, boolean showStaff) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(report.getTimestamp().toInstant(), ZoneOffset.UTC);
        String time = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String reporter = report.getReporterName() + "\n[" + report.getReporterUuid() + "]";
        String culprit = report.getCulpritUuid() != null ? report.getCulpritName() + "\n[" + report.getCulpritUuid() + "]" : "Unknown";

        ArrayList<DiscordMessageField> fields = new ArrayList<>();
        fields.add(new DiscordMessageField("Reporter", reporter, true));
        fields.add(new DiscordMessageField("Culprit", culprit, true));
        if(showStaff) {
            String staff = report.getStaffName() + "\n[" + report.getStaffUuid() + "]";
            fields.add(new DiscordMessageField("Staff", staff));
        }
        fields.add(new DiscordMessageField("Reason", "```" + report.getReason() + "```"));
        fields.add(new DiscordMessageField("Status", "**" + report.getReportStatus() + "**"));

        discordClient.sendEvent(new DiscordMessage("Report update from StaffPlusPlus", new DiscordMessageEmbed(
                title,
                "https://www.spigotmc.org/resources/staff.83562/",
                color,
                time,
                new DiscordMessageFooter("Provided by StaffPlusPlus", "https://cdn.discordapp.com/embed/avatars/0.png"),
                fields
        )));
    }
}
