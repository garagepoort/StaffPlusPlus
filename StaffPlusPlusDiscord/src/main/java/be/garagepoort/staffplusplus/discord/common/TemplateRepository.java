package be.garagepoort.staffplusplus.discord.common;

import be.garagepoort.staffplusplus.discord.StaffPlusPlusDiscord;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static java.io.File.separator;
import static java.util.Arrays.stream;

public class TemplateRepository {

    private static final String PATH_PREFIX = StaffPlusPlusDiscord.get().getDataFolder() + separator;
    private final Map<String, String> templates = new HashMap<>();

    private static final TemplateFile[] templateFiles = {
        new TemplateFile("reports", "report-created"),
        new TemplateFile("reports", "report-accepted"),
        new TemplateFile("reports", "report-resolved"),
        new TemplateFile("reports", "report-rejected"),
        new TemplateFile("reports", "report-reopened"),
        new TemplateFile("warnings", "warning-created"),
        new TemplateFile("warnings", "threshold-reached"),
        new TemplateFile("appeals", "appeal-created"),
        new TemplateFile("appeals", "appeal-approved"),
        new TemplateFile("appeals", "appeal-rejected"),
        new TemplateFile("bans", "banned"),
        new TemplateFile("bans", "unbanned"),
        new TemplateFile("mutes", "muted"),
        new TemplateFile("mutes", "unmuted"),
        new TemplateFile("kicks", "kicked"),
        new TemplateFile("altdetects", "detected"),
        new TemplateFile("staffmode", "enter-staffmode"),
        new TemplateFile("staffmode", "exit-staffmode")
    };

    public TemplateRepository(StaffPlusPlusDiscord plugin, FileConfiguration config) {
        boolean replaceTemplates = config.getBoolean("StaffPlusPlusDiscord.updateTemplates", true);
        String templatePack = config.getString("StaffPlusPlusDiscord.templatePack", "default");

        stream(templateFiles)
            .filter(templateFile -> replaceTemplates || !new File(plugin.getDataFolder(), templateFile.getResourcePath(templatePack)).exists())
            .forEach(templateFile -> plugin.saveResource(templateFile.getResourcePath(templatePack), replaceTemplates));

        for (TemplateFile templateFile : templateFiles) {
            templates.put(templateFile.getId(), Utils.readTemplate(PATH_PREFIX + templateFile.getResourcePath(templatePack)));
        }
    }

    public String getTemplate(String key) {
        if (!templates.containsKey(key)) {
            throw new RuntimeException("No template found with key: [" + key + "]");
        }
        return templates.get(key);
    }
}