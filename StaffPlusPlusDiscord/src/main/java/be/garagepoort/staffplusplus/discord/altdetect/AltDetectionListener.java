package be.garagepoort.staffplusplus.discord.altdetect;

import be.garagepoort.staffplusplus.discord.Constants;
import be.garagepoort.staffplusplus.discord.api.*;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import net.shortninja.staffplus.event.altdetect.AltDetectEvent;
import net.shortninja.staffplus.unordered.altdetect.AltDetectTrustLevel;
import net.shortninja.staffplus.unordered.altdetect.IAltDetectResult;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AltDetectionListener implements Listener {

    private static final String BAN_COLOR = "16601379";
    private DiscordClient discordClient;
    private FileConfiguration config;
    private List<AltDetectTrustLevel> enabledTrustLevels;

    public AltDetectionListener(FileConfiguration config) {
        this.config = config;
    }

    public void init() {
        enabledTrustLevels = Arrays.stream(config.getString("StaffPlusPlusDiscord.altDetect.enabledTrustLevels", "").split(";"))
            .map(AltDetectTrustLevel::valueOf)
            .collect(Collectors.toList());

        discordClient = Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .logger(new Slf4jLogger(DiscordClient.class))
            .logLevel(Logger.Level.FULL)
            .target(DiscordClient.class, config.getString("StaffPlusPlusDiscord.altDetect.webhookUrl", ""));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleAltDetectionEvent(AltDetectEvent event) {
        IAltDetectResult altDetectResult = event.getAltDetectResult();
        if(enabledTrustLevels.contains(altDetectResult.getAltDetectTrustLevel())) {
            buildMessage(altDetectResult, "Alt Account detected: " + altDetectResult.getPlayerCheckedName(), BAN_COLOR);
        }
    }

    private void buildMessage(IAltDetectResult detectResult, String title, String color) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String player1 = detectResult.getPlayerCheckedName() + "\n[" + detectResult.getPlayerCheckedUuid() + "]";
        String player2 = detectResult.getPlayerMatchedName() + "\n[" + detectResult.getPlayerMatchedUuid() + "]";

        ArrayList<DiscordMessageField> fields = new ArrayList<>();
        fields.add(new DiscordMessageField("Player checked", player1, true));
        fields.add(new DiscordMessageField("Player matched:", player2, true));

        fields.add(new DiscordMessageField("Trust Score", detectResult.getAltDetectTrustLevel().name()));

        sendEvent(title, color, time, fields);
    }

    private void sendEvent(String title, String color, String time, ArrayList<DiscordMessageField> fields) {
        discordClient.sendEvent(new DiscordMessage("Alt Account detection from Staff++", new DiscordMessageEmbed(
            title,
            Constants.STAFFPLUSPLUS_URL,
            color,
            time,
            DiscordUtil.createFooter(),
            fields
        )));
    }

    public boolean isEnabled() {
        String trustLevels = config.getString("StaffPlusPlusDiscord.altDetect.enabledTrustLevels");
        return trustLevels != null && !trustLevels.isEmpty();
    }
}
