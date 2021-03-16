package be.garagepoort.staffplusplus.discord.common;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

import static be.garagepoort.staffplusplus.discord.common.TemplateResourceUtil.getFullTemplatePath;
import static java.util.Arrays.stream;

public class TemplateRepository {

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
        new TemplateFile("staffmode", "exit-staffmode"),
        new TemplateFile("chat", "chat-phrase-detected"),
        new TemplateFile("xray", "xray")
    };

    public TemplateRepository(FileConfiguration config) {
        boolean replaceTemplates = config.getBoolean("StaffPlusPlusDiscord.updateTemplates", true);
        String templatePack = config.getString("StaffPlusPlusDiscord.templatePack", "default");

        stream(templateFiles)
            .forEach(templateFile -> TemplateResourceUtil.saveTemplate(templatePack, templateFile.getFilePath(), replaceTemplates));

        for (TemplateFile templateFile : templateFiles) {
            templates.put(templateFile.getId(), Utils.readTemplate(getFullTemplatePath(templatePack, templateFile)));
        }
    }

    public String getTemplate(String key) {
        if (!templates.containsKey(key)) {
            throw new RuntimeException("No template found with key: [" + key + "]");
        }
        return templates.get(key);
    }
}