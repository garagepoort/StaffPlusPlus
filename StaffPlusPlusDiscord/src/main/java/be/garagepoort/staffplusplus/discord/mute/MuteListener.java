package be.garagepoort.staffplusplus.discord.mute;

import be.garagepoort.staffplusplus.discord.api.DiscordClient;
import be.garagepoort.staffplusplus.discord.api.DiscordMessageField;
import be.garagepoort.staffplusplus.discord.api.DiscordUtil;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import net.shortninja.staffplus.event.mute.MuteEvent;
import net.shortninja.staffplus.event.mute.UnmuteEvent;
import net.shortninja.staffplus.unordered.IMute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MuteListener implements Listener {

    private static final String UNMUTE_COLOR = "16620323";
    private static final String MUTE_COLOR = "16601379";
    private DiscordClient discordClient;
    private FileConfiguration config;

    public MuteListener(FileConfiguration config) {
        this.config = config;
    }

    public void init() {
        discordClient = Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .logger(new Slf4jLogger(DiscordClient.class))
            .logLevel(Logger.Level.FULL)
            .target(DiscordClient.class, config.getString("StaffPlusPlusDiscord.mutes.webhookUrl", ""));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleMuteEvent(MuteEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.mutes.mute")) {
            return;
        }

        IMute mute = event.getMute();
        buildMessage(mute, "User muted: " + mute.getPlayerName(), MUTE_COLOR);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleUnmuteEvent(UnmuteEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.mutes.unmute")) {
            return;
        }

        buildMessage(event.getMute(), "User unmuted: " + event.getMute().getPlayerName(), UNMUTE_COLOR);
    }

    private void buildMessage(IMute mute, String title, String color) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(mute.getCreationDate().toInstant(), ZoneOffset.UTC);
        String time = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String issuer = mute.getIssuerName() + "\n[" + mute.getIssuerUuid() + "]";
        String muted = mute.getPlayerName() + "\n[" + mute.getPlayerUuid() + "]";

        ArrayList<DiscordMessageField> fields = new ArrayList<>();
        fields.add(new DiscordMessageField("Issuer", issuer, true));
        fields.add(new DiscordMessageField("Muted:", muted, true));
        
        if (mute.getEndDate() != null) {
            LocalDateTime endDateTime = LocalDateTime.ofInstant(mute.getEndDate().toInstant(), ZoneOffset.UTC);
            String endTime = endDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            fields.add(new DiscordMessageField("Type", "TEMPORARY"));
            fields.add(new DiscordMessageField("Ends at: ", endTime, true));
        } else {
            fields.add(new DiscordMessageField("Type", "PERMANENT"));
        }

        fields.add(new DiscordMessageField("Mute Reason", "```" + mute.getReason() + "```"));

        if (mute.getUnmutedByUuid() != null) {
            String unmuter = mute.getUnmutedByName() + "\n[" + mute.getUnmutedByUuid() + "]";
            fields.add(new DiscordMessageField("Unmuted by", unmuter));
            fields.add(new DiscordMessageField("Unmute Reason", "```" + mute.getUnmuteReason() + "```"));
        }

        sendEvent(title, color, time, fields);
    }

    private void sendEvent(String title, String color, String time, ArrayList<DiscordMessageField> fields) {
        DiscordUtil.sendEvent(discordClient, "Mute update from Staff++", title, color, time, fields);
    }

    public boolean isEnabled() {
        return config.getBoolean("StaffPlusPlusDiscord.mutes.mute") ||
            config.getBoolean("StaffPlusPlusDiscord.mutes.unmute");
    }
}
