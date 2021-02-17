package be.garagepoort.staffplusplus.discord.kick;

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
import net.shortninja.staffplus.event.kick.KickEvent;
import net.shortninja.staffplus.unordered.IKick;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class KickListener implements StaffPlusPlusListener {

    private static final String KICK_COLOR = "16601379";
    private DiscordClient discordClient;
    private FileConfiguration config;

    public KickListener(FileConfiguration config) {
        this.config = config;
    }

    public void init() {
        discordClient = Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .logger(new Slf4jLogger(DiscordClient.class))
            .logLevel(Logger.Level.FULL)
            .target(DiscordClient.class, config.getString("StaffPlusPlusDiscord.kicks.webhookUrl", ""));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleKickEvent(KickEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.kicks.kick")) {
            return;
        }

        IKick kick = event.getKick();
        buildMessage(kick, "User kicked: " + kick.getPlayerName(), KICK_COLOR);
    }

    private void buildMessage(IKick kick, String title, String color) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(kick.getCreationDate().toInstant(), ZoneOffset.UTC);
        String time = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String issuer = kick.getIssuerName() + "\n[" + kick.getIssuerUuid() + "]";
        String kicked = kick.getPlayerName() + "\n[" + kick.getPlayerUuid() + "]";

        ArrayList<DiscordMessageField> fields = new ArrayList<>();
        fields.add(new DiscordMessageField("Issuer", issuer, true));
        fields.add(new DiscordMessageField("Kicked:", kicked, true));
        fields.add(new DiscordMessageField("Kick Reason", "```" + kick.getReason() + "```"));
        sendEvent(title, color, time, fields);
    }

    private void sendEvent(String title, String color, String time, ArrayList<DiscordMessageField> fields) {
        DiscordUtil.sendEvent(discordClient, "Kick update from Staff++", title, color, time, fields);
    }

    public boolean isEnabled() {
        return config.getBoolean("StaffPlusPlusDiscord.kicks.kick");
    }

    @Override
    public boolean isValid() {
        return StringUtils.isNotBlank(config.getString("StaffPlusPlusDiscord.kicks.webhookUrl"));
    }
}
