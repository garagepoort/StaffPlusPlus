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
import net.shortninja.staffplus.event.warnings.WarningAppealApprovedEvent;
import net.shortninja.staffplus.event.warnings.WarningAppealRejectedEvent;
import net.shortninja.staffplus.event.warnings.WarningAppealedEvent;
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

public class AppealListener implements StaffPlusPlusListener {

    private static final String APPEALS_PREFIX = "StaffPlusPlusDiscord.warnings.appeals";
    private static final String TEMPLATE_PATH = StaffPlusPlusDiscord.get().getDataFolder() + separator + "discordtemplates" + separator + "warnings" + separator + "appeals" + separator;
    private DiscordClient discordClient;
    private FileConfiguration config;

    public AppealListener(FileConfiguration config) {
        this.config = config;
    }

    public void init() {
        discordClient = Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .logger(new Slf4jLogger(DiscordClient.class))
            .logLevel(Logger.Level.FULL)
            .target(DiscordClient.class, config.getString(APPEALS_PREFIX + ".webhookUrl", ""));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleCreateAppeal(WarningAppealedEvent event) {
        if (!config.getBoolean(APPEALS_PREFIX + ".notifyCreate")) {
            return;
        }

        buildAppeal(event.getWarning(), "appeal-created.json");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleAppealApproved(WarningAppealApprovedEvent event) {
        if (!config.getBoolean(APPEALS_PREFIX + ".notifyApproved")) {
            return;
        }

        buildAppeal(event.getWarning(), "appeal-approved.json");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleAppealRejected(WarningAppealRejectedEvent event) {
        if (!config.getBoolean(APPEALS_PREFIX + ".notifyRejected")) {
            return;
        }

        buildAppeal(event.getWarning(), "appeal-rejected.json");
    }

    private void buildAppeal(IWarning warning, String templateFile) {
        String path = TEMPLATE_PATH + templateFile;
        LocalDateTime localDateTime = LocalDateTime.ofInstant(warning.getTimestamp().toInstant(), ZoneOffset.UTC);
        JexlContext jc = new MapContext();
        jc.set("warning", warning);
        jc.set("appeal", warning.getAppeal().get());
        jc.set("timestamp", localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        String template = JexlTemplateParser.parse(Utils.readTemplate(path), jc);
        DiscordUtil.sendEvent(discordClient, template);
    }

    public boolean isEnabled() {
        return config.getBoolean(APPEALS_PREFIX + ".notifyCreate");
    }

    @Override
    public boolean isValid() {
        return StringUtils.isNotBlank(config.getString(APPEALS_PREFIX + ".webhookUrl"));
    }
}
