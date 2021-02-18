package be.garagepoort.staffplusplus.discord.altdetect;

import be.garagepoort.staffplusplus.discord.StaffPlusPlusDiscord;
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
import net.shortninja.staffplus.event.altdetect.AltDetectEvent;
import net.shortninja.staffplus.unordered.altdetect.AltDetectTrustLevel;
import net.shortninja.staffplus.unordered.altdetect.IAltDetectResult;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.io.File.separator;

public class AltDetectionListener implements StaffPlusPlusListener {

    private static final String TEMPLATE_PATH = StaffPlusPlusDiscord.get().getDataFolder() + separator + "discordtemplates" + separator + "altdetects" + separator;
    private DiscordClient discordClient;
    private FileConfiguration config;
    private final TemplateRepository templateRepository;
    private List<AltDetectTrustLevel> enabledTrustLevels;

    public AltDetectionListener(FileConfiguration config, TemplateRepository templateRepository)  {
        this.config = config;
        this.templateRepository = templateRepository;
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
            buildDetectionResult(event.getAltDetectResult(), "altdetects/detected");
        }
    }

    private void buildDetectionResult(IAltDetectResult detectionResult, String templateFile) {

        String time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        JexlContext jc = new MapContext();
        jc.set("detectionResult", detectionResult);
        jc.set("timestamp", time);
        String template = JexlTemplateParser.parse(templateRepository.getTemplate(templateFile), jc);
        DiscordUtil.sendEvent(discordClient, template);
    }

    public boolean isEnabled() {
        String trustLevels = config.getString("StaffPlusPlusDiscord.altDetect.enabledTrustLevels");
        return trustLevels != null && !trustLevels.isEmpty();
    }

    @Override
    public boolean isValid() {
        return StringUtils.isNotBlank(config.getString("StaffPlusPlusDiscord.altDetect.webhookUrl"));
    }
}
