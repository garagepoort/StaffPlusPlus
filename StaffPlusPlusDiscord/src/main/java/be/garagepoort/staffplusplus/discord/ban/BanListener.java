package be.garagepoort.staffplusplus.discord.ban;

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
import net.shortninja.staffplus.event.ban.BanEvent;
import net.shortninja.staffplus.event.ban.UnbanEvent;
import net.shortninja.staffplus.unordered.IBan;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BanListener implements StaffPlusPlusListener {

    private static final String UNBAN_COLOR = "16620323";
    private static final String BAN_COLOR = "16601379";
    private DiscordClient discordClient;
    private FileConfiguration config;

    public BanListener(FileConfiguration config) {
        this.config = config;
    }

    public void init() {
        discordClient = Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .logger(new Slf4jLogger(DiscordClient.class))
            .logLevel(Logger.Level.FULL)
            .target(DiscordClient.class, config.getString("StaffPlusPlusDiscord.bans.webhookUrl", ""));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleBanEvent(BanEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.bans.ban")) {
            return;
        }

        IBan ban = event.getBan();
        buildMessage(ban, "User banned: " + ban.getPlayerName(), BAN_COLOR);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleUnbanEvent(UnbanEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.bans.unban")) {
            return;
        }

        buildMessage(event.getBan(), "User unbanned: " + event.getBan().getPlayerName(), UNBAN_COLOR);
    }

    private void buildMessage(IBan ban, String title, String color) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(ban.getCreationDate().toInstant(), ZoneOffset.UTC);
        String time = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String issuer = ban.getIssuerName() + "\n[" + ban.getIssuerUuid() + "]";
        String banned = ban.getPlayerName() + "\n[" + ban.getPlayerUuid() + "]";

        ArrayList<DiscordMessageField> fields = new ArrayList<>();
        fields.add(new DiscordMessageField("Issuer", issuer, true));
        fields.add(new DiscordMessageField("Banned:", banned, true));
        
        if (ban.getEndDate() != null) {
            LocalDateTime endDateTime = LocalDateTime.ofInstant(ban.getEndDate().toInstant(), ZoneOffset.UTC);
            String endTime = endDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            fields.add(new DiscordMessageField("Type", "TEMPORARY"));
            fields.add(new DiscordMessageField("Ends at: ", endTime, true));
        } else {
            fields.add(new DiscordMessageField("Type", "PERMANENT"));
        }

        fields.add(new DiscordMessageField("Ban Reason", "```" + ban.getReason() + "```"));

        if (ban.getUnbannedByUuid() != null) {
            String unbanner = ban.getUnbannedByName() + "\n[" + ban.getUnbannedByUuid() + "]";
            fields.add(new DiscordMessageField("Unbanned by", unbanner));
            fields.add(new DiscordMessageField("Unban Reason", "```" + ban.getUnbanReason() + "```"));
        }

        sendEvent(title, color, time, fields);
    }

    private void sendEvent(String title, String color, String time, ArrayList<DiscordMessageField> fields) {
        DiscordUtil.sendEvent(discordClient, "Ban update from Staff++", title, color, time, fields);
    }

    public boolean isEnabled() {
        return config.getBoolean("StaffPlusPlusDiscord.bans.ban") ||
            config.getBoolean("StaffPlusPlusDiscord.bans.unban");
    }

    @Override
    public boolean isValid() {
        return StringUtils.isNotBlank(config.getString("StaffPlusPlusDiscord.bans.webhookUrl"));
    }
}
