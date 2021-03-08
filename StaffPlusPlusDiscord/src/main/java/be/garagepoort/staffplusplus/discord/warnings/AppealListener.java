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
import net.shortninja.staffplusplus.warnings.WarningAppealApprovedEvent;
import net.shortninja.staffplusplus.warnings.WarningAppealRejectedEvent;
import net.shortninja.staffplusplus.warnings.WarningAppealedEvent;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class AppealListener implements StaffPlusPlusListener {

    private static final String APPEALS_PREFIX = "StaffPlusPlusDiscord.warnings.appeals";
    private DiscordClient discordClient;
    private FileConfiguration config;
    private final TemplateRepository templateRepository;

    public AppealListener(FileConfiguration config, TemplateRepository templateRepository)  {
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
            .target(DiscordClient.class, config.getString(APPEALS_PREFIX + ".webhookUrl", ""));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleCreateAppeal(WarningAppealedEvent event) {
        if (!config.getBoolean(APPEALS_PREFIX + ".notifyCreate")) {
            return;
        }

        buildAppeal(event.getWarning(), "appeals/appeal-created");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleAppealApproved(WarningAppealApprovedEvent event) {
        if (!config.getBoolean(APPEALS_PREFIX + ".notifyApproved")) {
            return;
        }

        buildAppeal(event.getWarning(), "appeals/appeal-approved");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleAppealRejected(WarningAppealRejectedEvent event) {
        if (!config.getBoolean(APPEALS_PREFIX + ".notifyRejected")) {
            return;
        }

        buildAppeal(event.getWarning(), "appeals/appeal-rejected");
    }

    private void buildAppeal(IWarning warning, String templateFile) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(warning.getCreationDate().toInstant(), ZoneOffset.UTC);
        JexlContext jc = new MapContext();
        jc.set("warning", warning);
        jc.set("appeal", warning.getAppeal().get());
        jc.set("timestamp", localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        String template = JexlTemplateParser.parse(templateRepository.getTemplate(templateFile), jc);
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
