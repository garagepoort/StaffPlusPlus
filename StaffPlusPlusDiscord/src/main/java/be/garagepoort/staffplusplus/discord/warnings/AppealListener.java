package be.garagepoort.staffplusplus.discord.warnings;

import be.garagepoort.staffplusplus.discord.StaffPlusPlusListener;
import be.garagepoort.staffplusplus.discord.api.DiscordClient;
import be.garagepoort.staffplusplus.discord.api.DiscordMessageField;
import be.garagepoort.staffplusplus.discord.api.DiscordUtil;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import net.shortninja.staffplus.event.warnings.WarningAppealedEvent;
import net.shortninja.staffplus.unordered.IWarning;
import net.shortninja.staffplus.unordered.IWarningAppeal;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class AppealListener implements StaffPlusPlusListener {

    private static final String APPEALS_PREFIX = "StaffPlusPlusDiscord.warnings.appeals";

    private static final String CLEAR_COLOR = "6431896";
    private static final String CREATE_COLOR = "16620323";
    private static final String THRESHOLD_COLOR = "16601379";
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
    public void handleCreateWAppeal(WarningAppealedEvent event) {
        if (!config.getBoolean(APPEALS_PREFIX + ".notifyCreate")) {
            return;
        }

        IWarning warning = event.getWarning();
        Optional<? extends IWarningAppeal> appeal = warning.getAppeal();
        appeal.ifPresent(iWarningAppeal -> buildWarningMessage(warning, "Warning appealed by: " + iWarningAppeal.getAppealerName(), CREATE_COLOR));
    }

    private void buildWarningMessage(IWarning warning, String title, String color) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(warning.getTimestamp().toInstant(), ZoneOffset.UTC);
        String time = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String appealer = warning.getAppeal().get().getAppealerName() + "\n[" + warning.getAppeal().get().getAppealerUuid() + "]";
        String issuer = warning.getIssuerName() + "\n[" + warning.getIssuerUuid() + "]";
        String culprit = warning.getName() + "\n[" + warning.getUuid() + "]";

        ArrayList<DiscordMessageField> fields = new ArrayList<>();
        fields.add(new DiscordMessageField("Appealer", appealer, true));
        fields.add(new DiscordMessageField("Appeal Reason", "```" + warning.getAppeal().get().getReason() + "```"));

        fields.add(new DiscordMessageField("Warning Info", "----------------------------------------------"));
        fields.add(new DiscordMessageField("Severity", "**" + warning.getSeverity() + "(" + warning.getScore() + ")**"));
        fields.add(new DiscordMessageField("Issuer", issuer, true));
        fields.add(new DiscordMessageField("Culprit", culprit, true));
        fields.add(new DiscordMessageField("Reason", "```" + warning.getReason() + "```"));

        sendEvent(title, color, time, fields);
    }

    private void sendEvent(String title, String color, String time, ArrayList<DiscordMessageField> fields) {
        DiscordUtil.sendEvent(discordClient, "Warning Appeal update from Staff++", title, color, time, fields);
    }

    public boolean isEnabled() {
        return config.getBoolean(APPEALS_PREFIX + ".notifyCreate");
    }

    @Override
    public boolean isValid() {
        return StringUtils.isNotBlank(config.getString(APPEALS_PREFIX + ".webhookUrl"));
    }
}
