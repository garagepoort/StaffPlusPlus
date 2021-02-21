package be.garagepoort.staffplusplus.discord.staffmode;

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
import net.shortninja.staffplus.event.staffmode.EnterStaffModeEvent;
import net.shortninja.staffplus.event.staffmode.ExitStaffModeEvent;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StaffModeListener implements StaffPlusPlusListener {

    private DiscordClient discordClient;
    private FileConfiguration config;
    private final TemplateRepository templateRepository;

    public StaffModeListener(FileConfiguration config, TemplateRepository templateRepository)  {
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
            .target(DiscordClient.class, config.getString("StaffPlusPlusDiscord.staffmode.webhookUrl", ""));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handEnterStaffMode(EnterStaffModeEvent event) {
        if(!config.getBoolean("StaffPlusPlusDiscord.staffmode.notify-enter")) {
            return;
        }
        buildEnterStaffModeResult(event, "staffmode/enter-staffmode");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handEnterStaffMode(ExitStaffModeEvent event) {
        if(!config.getBoolean("StaffPlusPlusDiscord.staffmode.notify-exit")) {
            return;
        }
        buildEnterStaffModeResult(event, "staffmode/exit-staffmode");
    }

    private void buildEnterStaffModeResult(Event event, String templateFile) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        JexlContext jc = new MapContext();
        jc.set("staffmodeEvent", event);
        jc.set("timestamp", time);
        String template = JexlTemplateParser.parse(templateRepository.getTemplate(templateFile), jc);
        DiscordUtil.sendEvent(discordClient, template);
    }

    public boolean isEnabled() {
        return config.getBoolean("StaffPlusPlusDiscord.staffmode.notify-enter") ||
            config.getBoolean("StaffPlusPlusDiscord.staffmode.notify-exit");
    }

    @Override
    public boolean isValid() {
        return StringUtils.isNotBlank(config.getString("StaffPlusPlusDiscord.staffmode.webhookUrl"));
    }
}
