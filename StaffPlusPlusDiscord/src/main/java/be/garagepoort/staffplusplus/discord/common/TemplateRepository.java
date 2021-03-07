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
       new TemplateFile("reports" + separator + "report-created"),
       new TemplateFile("reports" + separator + "report-accepted"),
       new TemplateFile("reports" + separator + "report-resolved"),
       new TemplateFile("reports" + separator + "report-rejected"),
       new TemplateFile("reports" + separator + "report-reopened"),
       new TemplateFile("warnings" + separator + "warning-created"),
       new TemplateFile("warnings" + separator + "threshold-reached"),
       new TemplateFile("appeals" + separator + "appeal-created"),
       new TemplateFile("appeals" + separator + "appeal-approved"),
       new TemplateFile("appeals" + separator + "appeal-rejected"),
       new TemplateFile("bans" + separator + "banned"),
       new TemplateFile("bans" + separator + "unbanned"),
       new TemplateFile("mutes" + separator + "muted"),
       new TemplateFile("mutes" + separator + "unmuted"),
       new TemplateFile("kicks" + separator + "kicked"),
       new TemplateFile("altdetects" + separator + "detected"),
       new TemplateFile("staffmode" + separator + "enter-staffmode"),
       new TemplateFile("staffmode" + separator + "exit-staffmode")
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