package be.garagepoort.staffplusplus.discord.ban;

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
import net.shortninja.staffplus.event.ban.BanEvent;
import net.shortninja.staffplus.event.ban.UnbanEvent;
import net.shortninja.staffplus.unordered.IBan;
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

public class BanListener implements StaffPlusPlusListener {

    private static final String TEMPLATE_PATH = StaffPlusPlusDiscord.get().getDataFolder() + separator + "discordtemplates" + separator + "bans" + separator;
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

        buildBan(event.getBan(), "banned.json");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleUnbanEvent(UnbanEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.bans.unban")) {
            return;
        }

        buildBan(event.getBan(), "unbanned.json");
    }

    private void buildBan(IBan ban, String templateFile) {
        String path = TEMPLATE_PATH + templateFile;
        LocalDateTime localDateTime = LocalDateTime.ofInstant(ban.getCreationDate().toInstant(), ZoneOffset.UTC);
        JexlContext jc = new MapContext();
        jc.set("ban", ban);
        jc.set("timestamp", localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        String template = JexlTemplateParser.parse(Utils.readTemplate(path), jc);
        DiscordUtil.sendEvent(discordClient, template);
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
