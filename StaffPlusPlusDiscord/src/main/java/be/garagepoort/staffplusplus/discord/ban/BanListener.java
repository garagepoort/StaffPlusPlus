package be.garagepoort.staffplusplus.discord.ban;

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
import net.shortninja.staffplusplus.ban.BanEvent;
import net.shortninja.staffplusplus.ban.IBan;
import net.shortninja.staffplusplus.ban.UnbanEvent;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class BanListener implements StaffPlusPlusListener {

    private DiscordClient discordClient;
    private FileConfiguration config;
    private final TemplateRepository templateRepository;

    public BanListener(FileConfiguration config, TemplateRepository templateRepository)  {
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
            .target(DiscordClient.class, config.getString("StaffPlusPlusDiscord.bans.webhookUrl", ""));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleBanEvent(BanEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.bans.ban")) {
            return;
        }

        buildBan(event.getBan(), "bans/banned");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleUnbanEvent(UnbanEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.bans.unban")) {
            return;
        }

        buildBan(event.getBan(), "bans/unbanned");
    }

    private void buildBan(IBan ban, String templateFile) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(ban.getCreationDate().toInstant(), ZoneOffset.UTC);
        JexlContext jc = new MapContext();
        jc.set("ban", ban);
        jc.set("timestamp", localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        String template = JexlTemplateParser.parse(templateRepository.getTemplate(templateFile), jc);
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
