package be.garagepoort.staffplusplus.discord.warnings;

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
import net.shortninja.staffplusplus.warnings.IWarning;
import net.shortninja.staffplusplus.warnings.WarningCreatedEvent;
import net.shortninja.staffplusplus.warnings.WarningThresholdReachedEvent;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class WarningListener implements StaffPlusPlusListener {

    private static final String WARNINGS_PREFIX = "StaffPlusPlusDiscord.warnings.";
    private DiscordClient discordClient;
    private FileConfiguration config;
    private final TemplateRepository templateRepository;

    public WarningListener(FileConfiguration config, TemplateRepository templateRepository)  {
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
            .target(DiscordClient.class, config.getString(WARNINGS_PREFIX + "webhookUrl", ""));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleCreateWarning(WarningCreatedEvent event) {
        if (!config.getBoolean(WARNINGS_PREFIX + "notifyCreate")) {
            return;
        }

        buildWarning(event.getWarning(), "warnings/warning-created");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleThresholdReachedWarning(WarningThresholdReachedEvent event) {
        if (!config.getBoolean(WARNINGS_PREFIX + "notifyThresholdReached")) {
            return;
        }

        buildThreshold(event, "warnings/threshold-reached");
    }

    private void buildWarning(IWarning warning, String templateFile) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(warning.getCreationDate().toInstant(), ZoneOffset.UTC);
        JexlContext jc = new MapContext();
        jc.set("warning", warning);
        jc.set("timestamp", localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        String template = JexlTemplateParser.parse(templateRepository.getTemplate(templateFile), jc);
        DiscordUtil.sendEvent(discordClient, template);
    }

    private void buildThreshold(WarningThresholdReachedEvent warning, String templateFile) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        JexlContext jc = new MapContext();
        jc.set("threshold", warning);
        jc.set("commandsTriggered", String.join("\n", warning.getCommandsTriggered()));
        jc.set("timestamp", time);

        String template = JexlTemplateParser.parse(templateRepository.getTemplate(templateFile), jc);
        DiscordUtil.sendEvent(discordClient, template);
    }

    public boolean isEnabled() {
        return config.getBoolean(WARNINGS_PREFIX + "notifyCreate") ||
            config.getBoolean(WARNINGS_PREFIX + "notifyCleared") ||
            config.getBoolean(WARNINGS_PREFIX + "notifyThresholdReached");
    }

    @Override
    public boolean isValid() {
        return StringUtils.isNotBlank(config.getString(WARNINGS_PREFIX + "webhookUrl"));
    }
}
