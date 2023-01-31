package net.shortninja.staffplus.core.punishments.ban.playerbans.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.TubingPlugin;
import be.garagepoort.mcioc.load.InjectTubingPlugin;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@IocBean
public class BanTemplateLoader {

    private final TubingPlugin tubingPlugin;

    public BanTemplateLoader(@InjectTubingPlugin TubingPlugin tubingPlugin) {
        this.tubingPlugin = tubingPlugin;
    }

    public Map<String, String> loadTemplates() {
        String directoryPath = tubingPlugin.getDataFolder() + File.separator + "bans";
        File banDir = new File(directoryPath);
        if (!banDir.exists()) {
            return Collections.emptyMap();
        }
        Map<String, String> templates = new HashMap<>();
        for (File file : Objects.requireNonNull(banDir.listFiles())) {
            templates.put(getFileNameWithoutExtension(file), readTemplate(file));
        }
        return templates;
    }

    private String getFileNameWithoutExtension(File file) {
        return file.getName().substring(0, file.getName().lastIndexOf('.'));
    }

    private String readTemplate(File filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(filePath.toPath(), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
        return contentBuilder.toString();
    }
}
