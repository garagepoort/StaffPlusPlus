package be.garagepoort.staffplusplus.discord.warnings;

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
import net.shortninja.staffplus.event.warnings.WarningCreatedEvent;
import net.shortninja.staffplus.event.warnings.WarningThresholdReachedEvent;
import net.shortninja.staffplus.unordered.IWarning;
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

public class WarningListener implements StaffPlusPlusListener {

    private static final String WARNINGS_PREFIX = "StaffPlusPlusDiscord.warnings.";
    private static final String TEMPLATE_PATH = StaffPlusPlusDiscord.get().getDataFolder() + separator + "discordtemplates" + separator + "warnings" + separator;
    private DiscordClient discordClient;
    private FileConfiguration config;

    public WarningListener(FileConfiguration config) {
        this.config = config;
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

        buildWarning(event.getWarning(), "warning-created.json");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleThresholdReachedWarning(WarningThresholdReachedEvent event) {
        if (!config.getBoolean(WARNINGS_PREFIX + "notifyThresholdReached")) {
            return;
        }

        buildThreshold(event, "threshold-reached.json");
    }

    private void buildWarning(IWarning warning, String templateFile) {
        String path = TEMPLATE_PATH + templateFile;
        LocalDateTime localDateTime = LocalDateTime.ofInstant(warning.getTimestamp().toInstant(), ZoneOffset.UTC);
        JexlContext jc = new MapContext();
        jc.set("warning", warning);
        jc.set("timestamp", localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        String template = JexlTemplateParser.parse(Utils.readTemplate(path), jc);
        DiscordUtil.sendEvent(discordClient, template);
    }

    private void buildThreshold(WarningThresholdReachedEvent warning, String templateFile) {
        String path = TEMPLATE_PATH + templateFile;
        String time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        JexlContext jc = new MapContext();
        jc.set("threshold", warning);
        jc.set("commandsTriggered", String.join("\n", warning.getCommandsTriggered()));
        jc.set("timestamp", time);

        String template = JexlTemplateParser.parse(Utils.readTemplate(path), jc);
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
