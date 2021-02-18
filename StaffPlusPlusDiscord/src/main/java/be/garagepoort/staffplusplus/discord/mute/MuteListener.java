package be.garagepoort.staffplusplus.discord.mute;

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
import net.shortninja.staffplus.event.mute.MuteEvent;
import net.shortninja.staffplus.event.mute.UnmuteEvent;
import net.shortninja.staffplus.unordered.IMute;
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

public class MuteListener implements StaffPlusPlusListener {

    private static final String TEMPLATE_PATH = StaffPlusPlusDiscord.get().getDataFolder() + separator + "discordtemplates" + separator + "mutes" + separator;
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
        buildMute(mute, "muted.json");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleUnmuteEvent(UnmuteEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.mutes.unmute")) {
            return;
        }

        buildMute(event.getMute(), "unmuted.json");
    }

    private void buildMute(IMute mute, String templateFile) {
        String path = TEMPLATE_PATH + templateFile;
        LocalDateTime localDateTime = LocalDateTime.ofInstant(mute.getCreationDate().toInstant(), ZoneOffset.UTC);
        JexlContext jc = new MapContext();
        jc.set("mute", mute);
        jc.set("timestamp", localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        String template = JexlTemplateParser.parse(Utils.readTemplate(path), jc);
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
