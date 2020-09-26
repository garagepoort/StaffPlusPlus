package be.garagepoort.staffplusplus.discord.warnings;

import be.garagepoort.staffplusplus.discord.Constants;
import be.garagepoort.staffplusplus.discord.api.*;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import net.shortninja.staffplus.event.warnings.WarningCreatedEvent;
import net.shortninja.staffplus.event.warnings.WarningThresholdReachedEvent;
import net.shortninja.staffplus.event.warnings.WarningsClearedEvent;
import net.shortninja.staffplus.unordered.IWarning;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WarningListener implements Listener {

    private static final String CLEAR_COLOR = "6431896";
    private static final String CREATE_COLOR = "16620323";
    private static final String THRESHOLD_COLOR = "16601379";
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
            .target(DiscordClient.class, config.getString("StaffPlusPlusDiscord.warnings.webhookUrl", ""));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleCreateWarning(WarningCreatedEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.warnings.notifyCreate")) {
            return;
        }

        IWarning warning = event.getWarning();
        buildWarningMessage(warning, "Warning issued by: " + warning.getIssuerName(), CREATE_COLOR);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleClearedWarning(WarningsClearedEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.warnings.notifyCleared")) {
            return;
        }

        buildClearedMessage(event, "Warnings cleared by: " + event.getIssuerName(), CLEAR_COLOR);
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void handleThresholdReachedWarning(WarningThresholdReachedEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.warnings.notifyThresholdReached")) {
            return;
        }

        buildThresholdReachedMessage(event, "Threshold reached by: " + event.getPlayerName(), THRESHOLD_COLOR);
    }

    private void buildWarningMessage(IWarning warning, String title, String color) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(warning.getTimestamp().toInstant(), ZoneOffset.UTC);
        String time = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String issuer = warning.getIssuerName() + "\n[" + warning.getIssuerUuid() + "]";
        String culprit = warning.getName() + "\n[" + warning.getUuid() + "]";

        ArrayList<DiscordMessageField> fields = new ArrayList<>();
        fields.add(new DiscordMessageField("Severity", "**" + warning.getSeverity() + "(" + warning.getScore() + ")**"));
        fields.add(new DiscordMessageField("Issuer", issuer, true));
        fields.add(new DiscordMessageField("Culprit", culprit, true));
        fields.add(new DiscordMessageField("Reason", "```" + warning.getReason() + "```"));

        sendEvent(title, color, time, fields);
    }

    private void buildClearedMessage(WarningsClearedEvent event, String title, String color) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String issuer = event.getIssuerName() + "\n[" + event.getIssuerUuid() + "]";
        String culprit = event.getPlayerName() + "\n[" + event.getPlayerUuid() + "]";

        ArrayList<DiscordMessageField> fields = new ArrayList<>();
        fields.add(new DiscordMessageField("Issuer", issuer, true));
        fields.add(new DiscordMessageField("Culprit", culprit, true));
        fields.add(new DiscordMessageField("Message", "All warnings removed for player: " + culprit));
        sendEvent(title, color, time, fields);
    }

    private void buildThresholdReachedMessage(WarningThresholdReachedEvent event, String title, String color) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String culprit = event.getPlayerName() + "\n[" + event.getPlayerUuid() + "]";

        ArrayList<DiscordMessageField> fields = new ArrayList<>();
        fields.add(new DiscordMessageField("Culprit", culprit, true));
        fields.add(new DiscordMessageField("Message", culprit + " has reached threshold of score: [" + event.getThresholdScore() + "]"));
        fields.add(new DiscordMessageField("Commands triggered", "```" + String.join("\n", event.getCommandsTriggered()) + "```"));
        sendEvent(title, color, time, fields);
    }

    private void sendEvent(String title, String color, String time, ArrayList<DiscordMessageField> fields) {
        discordClient.sendEvent(new DiscordMessage("Warning update from StaffPlusPlus", new DiscordMessageEmbed(
            title,
            Constants.STAFFPLUSPLUS_URL,
            color,
            time,
            DiscordUtil.createFooter(),
            fields
        )));
    }

    public boolean isEnabled() {
        return config.getBoolean("StaffPlusPlusDiscord.warnings.notifyCreate") ||
            config.getBoolean("StaffPlusPlusDiscord.warnings.notifyCleared") ||
            config.getBoolean("StaffPlusPlusDiscord.warnings.notifyThresholdReached");
    }
}
