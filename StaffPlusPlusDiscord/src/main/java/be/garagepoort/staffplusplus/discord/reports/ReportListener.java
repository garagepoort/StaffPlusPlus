package be.garagepoort.staffplusplus.discord.reports;

import be.garagepoort.staffplusplus.discord.StaffPlusPlusListener;
import be.garagepoort.staffplusplus.discord.api.DiscordClient;
import be.garagepoort.staffplusplus.discord.api.DiscordUtil;
import be.garagepoort.staffplusplus.discord.common.JexlTemplateParser;
import be.garagepoort.staffplusplus.discord.common.TemplateRepository;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import net.shortninja.staffplusplus.reports.*;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class ReportListener implements StaffPlusPlusListener {

    private DiscordClient discordClient;
    private FileConfiguration config;
    private final TemplateRepository templateRepository;

    public ReportListener(FileConfiguration config, TemplateRepository templateRepository)  {
        this.config = config;
        this.templateRepository = templateRepository;
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

        buildReport(event.getReport(), "reports/report-created");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleReopenReport(ReopenReportEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.notifyReopen")) {
            return;
        }

        buildReport(event.getReport(), "reports/report-reopened");
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void handleAcceptReport(AcceptReportEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.notifyAccept")) {
            return;
        }

        buildReport(event.getReport(), "reports/report-accepted");
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void handleRejectReport(RejectReportEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.notifyReject")) {
            return;
        }

        buildReport(event.getReport(), "reports/report-rejected");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleResolveReport(ResolveReportEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.notifyResolve")) {
            return;
        }

        buildReport(event.getReport(), "reports/report-resolved");
    }

    public void buildReport(IReport report, String key) {
        String createReportTemplate = replaceReportCreatedTemplate(report, templateRepository.getTemplate(key));
        DiscordUtil.sendEvent(discordClient, createReportTemplate);
    }

    private String replaceReportCreatedTemplate(IReport report, String createReportTemplate) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(report.getCreationDate().toInstant(), ZoneOffset.UTC);

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
