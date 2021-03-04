package be.garagepoort.staffplusplus.discord.trace;

import be.garagepoort.staffplusplus.discord.StaffPlusPlusDiscord;
import be.garagepoort.staffplusplus.discord.api.*;
import be.garagepoort.staffplusplus.discord.common.Constants;
import be.garagepoort.staffplusplus.discord.common.TemplateRepository;
import com.google.gson.Gson;
import feign.Feign;
import feign.Logger;
import feign.form.FormData;
import feign.form.FormEncoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import net.shortninja.staffplusplus.trace.ITrace;
import net.shortninja.staffplusplus.trace.StopTraceEvent;
import net.shortninja.staffplusplus.trace.TraceOutputChannel;
import net.shortninja.staffplusplus.trace.TraceWriter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static be.garagepoort.staffplusplus.discord.common.Constants.*;

public class TraceListener implements Listener {

    private static final String THRESHOLD_COLOR = "16601379";
    private DiscordClient discordClient;
    private FileConfiguration config;
    private final TemplateRepository templateRepository;

    public TraceListener(FileConfiguration config, TemplateRepository templateRepository)  {
        this.config = config;
        this.templateRepository = templateRepository;
    }

    public void init() {
        discordClient = Feign.builder()
            .client(new OkHttpClient())
            .encoder(new FormEncoder(new GsonEncoder()))
            .decoder(new GsonDecoder())
            .logger(new Slf4jLogger(DiscordClient.class))
            .logLevel(Logger.Level.FULL)
            .target(DiscordClient.class, config.getString(TRACE_WEBHOOK_URL, ""));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleTraceStopped(StopTraceEvent event) {
        if (!config.getBoolean(NOTIFY_STOP_TRACE)) {
            return;
        }

        if (config.getBoolean(INCLUDE_TRACE_LOG_FILE)) {
            try {
                File file = getTraceFile(event);
                if (file == null) return;
                buildTraceStoppedMessage(event.getTrace(), "Trace Stopped", THRESHOLD_COLOR, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            buildTraceStoppedMessage(event.getTrace(), "Trace Stopped", THRESHOLD_COLOR, null);
        }

    }

    private File getTraceFile(StopTraceEvent event) throws IOException {
        List<TraceWriter> writers = event.getTrace().getWriters();
        Optional<TraceWriter> traceWriter = writers.stream()
            .filter(tw -> tw.getType() == TraceOutputChannel.FILE)
            .findFirst();

        if (!traceWriter.isPresent()) {
            Bukkit.getLogger().warning("Could not notify traceFile. No FileTraceWriter found for trace");
            return null;
        }

        String resource = traceWriter.get().getResource();
        Path path = Paths.get(resource);

        return path.toFile();
    }

    private void buildTraceStoppedMessage(ITrace trace, String title, String color, File file) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String tracer = trace.getTracerUuid() + "\n[" + trace.getTracerUuid() + "]";
        String traced = trace.getTracedName() + "\n[" + trace.getTracedUuid() + "]";

        ArrayList<DiscordMessageField> fields = new ArrayList<>();
        fields.add(new DiscordMessageField("Tracer", tracer, true));
        fields.add(new DiscordMessageField("Traced", traced, true));

        if (file != null) {
            sendFileEvent(title, color, time, fields, file);
        } else {
            sendEvent(title, color, time, fields);
        }
    }

    private void sendFileEvent(String title, String color, String time, ArrayList<DiscordMessageField> fields, File file) {
        try {
            String discordMessage = new Gson().toJson(new DiscordMessage("Trace event from Staff++", getEmbed(title, color, time, fields)));
            discordClient.sendFileEvent(discordMessage, new FormData("text/plain", file.getName(), Files.readAllBytes(file.toPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendEvent(String title, String color, String time, ArrayList<DiscordMessageField> fields) {
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlusPlusDiscord.get(),
            () -> discordClient.sendEvent(new DiscordMessage("Trace event from Staff++", getEmbed(title, color, time, fields))));
    }

    private DiscordMessageEmbed getEmbed(String title, String color, String time, ArrayList<DiscordMessageField> fields) {
        return new DiscordMessageEmbed(
            title,
            Constants.STAFFPLUSPLUS_URL,
            color,
            time,
            DiscordUtil.createFooter(),
            fields
        );
    }

    public boolean isEnabled() {
        return config.getBoolean(NOTIFY_START_TRACE) ||
            config.getBoolean(NOTIFY_STOP_TRACE);
    }
}
