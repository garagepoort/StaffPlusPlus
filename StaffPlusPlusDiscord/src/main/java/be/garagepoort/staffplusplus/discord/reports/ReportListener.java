package be.garagepoort.staffplusplus.discord.reports;

import be.garagepoort.staffplusplus.discord.StaffPlusPlusDiscord;
import be.garagepoort.staffplusplus.discord.StaffPlusPlusListener;
import be.garagepoort.staffplusplus.discord.api.DiscordClient;
import be.garagepoort.staffplusplus.discord.api.DiscordUtil;
import be.garagepoort.staffplusplus.discord.common.JexlTemplateParser;
import be.garagepoort.staffplusplus.discord.common.Utils;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import net.shortninja.staffplus.event.*;
import net.shortninja.staffplus.unordered.IReport;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static java.io.File.separator;

public class ReportListener implements StaffPlusPlusListener {

    private static final String OPEN_COLOR = "6431896";
    private static final String ACCEPTED_COLOR = "16620323";
    private static final String REJECTED_COLOR = "16601379";
    private static final String RESOLVED_COLOR = "5027875";
    private static final String TEMPLATE_PATH = StaffPlusPlusDiscord.get().getDataFolder() + separator + "discordtemplates" + separator + "reports" + separator;
    private DiscordClient discordClient;
    private FileConfiguration config;

    public ReportListener(FileConfiguration config) {
        this.config = config;
    }

    public void init() {
        discordClient = Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .logger(new Slf4jLogger(DiscordClient.class))
            .logLevel(Logger.Level.FULL)
            .target(DiscordClient.class, config.getString("StaffPlusPlusDiscord.webhookUrl"));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleCreateReport(CreateReportEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.notifyOpen")) {
            return;
        }

        buildReport(event.getReport(), "report-created.json");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleReopenReport(ReopenReportEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.notifyReopen")) {
            return;
        }

        buildReport(event.getReport(), "report-reopened.json");
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void handleAcceptReport(AcceptReportEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.notifyAccept")) {
            return;
        }

        buildReport(event.getReport(), "report-accepted.json");
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void handleRejectReport(RejectReportEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.notifyReject")) {
            return;
        }

        buildReport(event.getReport(), "report-rejected.json");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleResolveReport(ResolveReportEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.notifyResolve")) {
            return;
        }

        buildReport(event.getReport(), "report-resolved.json");
    }

    public void buildReport(IReport report, String templateFile) {
        String path = TEMPLATE_PATH + templateFile;
        String createReportTemplate = replaceReportCreatedTemplate(report, Utils.readTemplate(path));
        DiscordUtil.sendEvent(discordClient, createReportTemplate);
    }

    private String replaceReportCreatedTemplate(IReport report, String createReportTemplate) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(report.getTimestamp().toInstant(), ZoneOffset.UTC);

        JexlContext jc = new MapContext();
        jc.set("report", report);
        jc.set("timestamp", localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        return JexlTemplateParser.parse(createReportTemplate, jc);
    }

    public boolean isEnabled() {
        return config.getBoolean("StaffPlusPlusDiscord.notifyOpen") ||
            config.getBoolean("StaffPlusPlusDiscord.notifyReopen") ||
            config.getBoolean("StaffPlusPlusDiscord.notifyAccept") ||
            config.getBoolean("StaffPlusPlusDiscord.notifyReject") ||
            config.getBoolean("StaffPlusPlusDiscord.notifyResolve");
    }

    @Override
    public boolean isValid() {
        return StringUtils.isNotBlank(config.getString("StaffPlusPlusDiscord.webhookUrl"));
    }
}
