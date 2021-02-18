package be.garagepoort.staffplusplus.discord.kick;

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
import net.shortninja.staffplus.event.kick.KickEvent;
import net.shortninja.staffplus.unordered.IKick;
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

public class KickListener implements StaffPlusPlusListener {

    private static final String TEMPLATE_PATH = StaffPlusPlusDiscord.get().getDataFolder() + separator + "discordtemplates" + separator + "kicks" + separator;
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

        buildKick(event.getKick(), "kicked.json");
    }

    private void buildKick(IKick kick, String templateFile) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(kick.getCreationDate().toInstant(), ZoneOffset.UTC);
        String time = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String path = TEMPLATE_PATH + templateFile;
        JexlContext jc = new MapContext();
        jc.set("kick", kick);
        jc.set("timestamp", time);
        String template = JexlTemplateParser.parse(Utils.readTemplate(path), jc);
        DiscordUtil.sendEvent(discordClient, template);
    }

    public boolean isEnabled() {
        return config.getBoolean("StaffPlusPlusDiscord.kicks.kick");
    }

    @Override
    public boolean isValid() {
        return StringUtils.isNotBlank(config.getString("StaffPlusPlusDiscord.kicks.webhookUrl"));
    }
}
