package be.garagepoort.staffplusplus.discord.trace;

import be.garagepoort.staffplusplus.discord.Constants;
import be.garagepoort.staffplusplus.discord.api.*;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import net.shortninja.staffplus.event.trace.StopTraceEvent;
import net.shortninja.staffplus.unordered.trace.ITrace;
import net.shortninja.staffplus.unordered.trace.TraceWriter;
import net.shortninja.staffplus.unordered.trace.TraceWriterType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TraceListener implements Listener {

    private static final String CLEAR_COLOR = "6431896";
    private static final String CREATE_COLOR = "16620323";
    private static final String THRESHOLD_COLOR = "16601379";
    private final DiscordClient discordClient;
    private final FileConfiguration config;

    public TraceListener(FileConfiguration config) {
        discordClient = Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .logger(new Slf4jLogger(DiscordClient.class))
            .logLevel(Logger.Level.FULL)
            .target(DiscordClient.class, config.getString("StaffPlusPlusDiscord.warnings.webhookUrl", ""));
        this.config = config;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleTraceStopped(StopTraceEvent event) {
        if (!config.getBoolean("StaffPlusPlusDiscord.trace.notifyStopTrace")) {
            return;
        }

        if (config.getBoolean("StaffPlusPlusDiscord.trace.notifyTraceLogFile")) {
            try {
                byte[] bytes = getTraceFile(event);
                if (bytes == null) return;
                buildTraceStoppedMessage(event.getTrace(), "Trace Stopped", THRESHOLD_COLOR, bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
                buildTraceStoppedMessage(event.getTrace(), "Trace Stopped", THRESHOLD_COLOR, null);
        }

    }

    private byte[] getTraceFile(StopTraceEvent event) throws IOException {
        List<TraceWriter> writers = event.getTrace().getWriters();
        Optional<TraceWriter> traceWriter = writers.stream()
            .filter(tw -> tw.getType() == TraceWriterType.FILE)
            .findFirst();

        if (!traceWriter.isPresent()) {
            Bukkit.getLogger().warning("Could not notify traceFile. No FileTraceWriter found for trace");
            return null;
        }

        String resource = traceWriter.get().getResource();
        Path path = Paths.get(resource);

        return Files.readAllBytes(path);
    }

    private void buildTraceStoppedMessage(ITrace trace, String title, String color, byte[] bytes) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String tracer = trace.getTracerUuid() + "\n[" + trace.getTracerUuid() + "]";
        String traced = trace.getTracedName() + "\n[" + trace.getTracedUuid() + "]";

        ArrayList<DiscordMessageField> fields = new ArrayList<>();
        fields.add(new DiscordMessageField("Tracer", tracer, true));
        fields.add(new DiscordMessageField("Traced", traced, true));

        if(bytes != null) {
            sendFileEvent(title, color, time, fields, bytes);
        }else{
            sendEvent(title, color, time, fields);
        }
    }

    private void sendFileEvent(String title, String color, String time, ArrayList<DiscordMessageField> fields, byte[] file) {
        discordClient.sendFileEvent(new DiscordFileMessage(
            file,
            new DiscordMessage("Trace file from StaffPlusPlus", getEmbed(title, color, time, fields))
        ));
    }

    private void sendEvent(String title, String color, String time, ArrayList<DiscordMessageField> fields) {
        discordClient.sendEvent(new DiscordMessage("Trace event from StaffPlusPlus", getEmbed(title, color, time, fields)));
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
        return config.getBoolean("StaffPlusPlusDiscord.trace.notifyStartTrace") ||
            config.getBoolean("StaffPlusPlusDiscord.trace.notifyStopTrace") ||
            config.getBoolean("StaffPlusPlusDiscord.trace.notifyTraceLogFile");
    }
}
