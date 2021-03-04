package be.garagepoort.staffplusplus.discord.mute;

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
import net.shortninja.staffplusplus.mute.IMute;
import net.shortninja.staffplusplus.mute.MuteEvent;
import net.shortninja.staffplusplus.mute.UnmuteEvent;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class MuteListener implements StaffPlusPlusListener {

    private DiscordClient discordClient;
    private FileConfiguration config;
    private final TemplateRepository templateRepository;

    public MuteListener(FileConfiguration config, TemplateRepository templateRepository)  {
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
            .target(DiscordClient.class, config.getString("StaffPlusPlusDiscord.mutes.webhookUrl", ""));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleMuteEvent(MuteEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.mutes.mute")) {
            return;
        }

        IMute mute = event.getMute();
        buildMute(mute, "mutes/muted");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleUnmuteEvent(UnmuteEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.mutes.unmute")) {
            return;
        }

        buildMute(event.getMute(), "mutes/unmuted");
    }

    private void buildMute(IMute mute, String templateFile) {

        LocalDateTime localDateTime = LocalDateTime.ofInstant(mute.getCreationDate().toInstant(), ZoneOffset.UTC);
        JexlContext jc = new MapContext();
        jc.set("mute", mute);
        jc.set("timestamp", localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        String template = JexlTemplateParser.parse(templateRepository.getTemplate(templateFile), jc);
        DiscordUtil.sendEvent(discordClient, template);
    }

    public boolean isEnabled() {
        return config.getBoolean("StaffPlusPlusDiscord.mutes.mute") ||
            config.getBoolean("StaffPlusPlusDiscord.mutes.unmute");
    }

    @Override
    public boolean isValid() {
        return StringUtils.isNotBlank(config.getString("StaffPlusPlusDiscord.mutes.webhookUrl"));
    }
}
