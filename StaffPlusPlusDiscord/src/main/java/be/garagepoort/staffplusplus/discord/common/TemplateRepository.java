package be.garagepoort.staffplusplus.discord.common;

import be.garagepoort.staffplusplus.discord.StaffPlusPlusDiscord;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

import static java.io.File.separator;

public class TemplateRepository {

    private static final String PATH_PREFIX = StaffPlusPlusDiscord.get().getDataFolder() + separator + "discordtemplates" + separator;
    private final Map<String, String> templates = new HashMap<>();

    public TemplateRepository(StaffPlusPlusDiscord plugin, FileConfiguration config) {
        boolean replaceTemplates = config.getBoolean("StaffPlusPlusDiscord.updateTemplates", true);
        String templatePack = config.getString("StaffPlusPlusDiscord.templatePack", "default");
        boolean testFile = plugin.getDataFolder().exists("discordtemplates" + separator + "template_version.yml")
        if (replaceTemplates || !testFile) {
            plugin.saveResource("discordtemplates" + separator + templatePack + separator + "reports" + separator + "report-created.json", replaceTemplates);
            plugin.saveResource("discordtemplates" + separator + templatePack + separator + "reports" + separator + "report-accepted.json", replaceTemplates);
            plugin.saveResource("discordtemplates" + separator + templatePack + separator + "reports" + separator + "report-resolved.json", replaceTemplates);
            plugin.saveResource("discordtemplates" + separator + templatePack + separator + "reports" + separator + "report-rejected.json", replaceTemplates);
            plugin.saveResource("discordtemplates" + separator + templatePack + separator + "reports" + separator + "report-reopened.json", replaceTemplates);
            plugin.saveResource("discordtemplates" + separator + templatePack + separator + "warnings" + separator + "warning-created.json", replaceTemplates);
            plugin.saveResource("discordtemplates" + separator + templatePack + separator + "warnings" + separator + "threshold-reached.json", replaceTemplates);
            plugin.saveResource("discordtemplates" + separator + templatePack + separator + "warnings" + separator + "appeals" + separator + "appeal-created.json", replaceTemplates);
            plugin.saveResource("discordtemplates" + separator + templatePack + separator + "warnings" + separator + "appeals" + separator + "appeal-approved.json", replaceTemplates);
            plugin.saveResource("discordtemplates" + separator + templatePack + separator + "warnings" + separator + "appeals" + separator + "appeal-rejected.json", replaceTemplates);
            plugin.saveResource("discordtemplates" + separator + templatePack + separator + "bans" + separator + "banned.json", replaceTemplates);
            plugin.saveResource("discordtemplates" + separator + templatePack + separator + "bans" + separator + "unbanned.json", replaceTemplates);
            plugin.saveResource("discordtemplates" + separator + templatePack + separator + "mutes" + separator + "muted.json", replaceTemplates);
            plugin.saveResource("discordtemplates" + separator + templatePack + separator + "mutes" + separator + "unmuted.json", replaceTemplates);
            plugin.saveResource("discordtemplates" + separator + templatePack + separator + "kicks" + separator + "kicked.json", replaceTemplates);
            plugin.saveResource("discordtemplates" + separator + templatePack + separator + "altdetects" + separator + "detected.json", replaceTemplates);
            plugin.saveResource("discordtemplates" + separator + templatePack + separator + "staffmode" + separator + "enter-staffmode.json", replaceTemplates);
            plugin.saveResource("discordtemplates" + separator + templatePack + separator + "staffmode" + separator + "exit-staffmode.json", replaceTemplates);
            plugin.saveResource("discordtemplates" + separator + "template_version.yml", replaceTemplates);
        }

        templates.put("reports/report-created", Utils.readTemplate(PATH_PREFIX + templatePack + separator + "reports" + separator + "report-created.json"));
        templates.put("reports/report-accepted", Utils.readTemplate(PATH_PREFIX + templatePack + separator + "reports" + separator + "report-accepted.json"));
        templates.put("reports/report-resolved", Utils.readTemplate(PATH_PREFIX + templatePack + separator + "reports" + separator + "report-resolved.json"));
        templates.put("reports/report-rejected", Utils.readTemplate(PATH_PREFIX + templatePack + separator + "reports" + separator + "report-rejected.json"));
        templates.put("reports/report-reopened", Utils.readTemplate(PATH_PREFIX + templatePack + separator + "reports" + separator + "report-reopened.json"));
        templates.put("warnings/warning-created", Utils.readTemplate(PATH_PREFIX + templatePack + separator + "warnings" + separator + "warning-created.json"));
        templates.put("warnings/threshold-reached", Utils.readTemplate(PATH_PREFIX + templatePack + separator + "warnings" + separator + "threshold-reached.json"));
        templates.put("warnings/appeals/appeal-created", Utils.readTemplate(PATH_PREFIX + templatePack + separator + "warnings" + separator + "appeals" + separator + "appeal-created.json"));
        templates.put("warnings/appeals/appeal-approved", Utils.readTemplate(PATH_PREFIX + templatePack + separator + "warnings" + separator + "appeals" + separator + "appeal-approved.json"));
        templates.put("warnings/appeals/appeal-rejected", Utils.readTemplate(PATH_PREFIX + templatePack + separator + "warnings" + separator + "appeals" + separator + "appeal-rejected.json"));
        templates.put("bans/banned", Utils.readTemplate(PATH_PREFIX + templatePack + separator + "bans" + separator + "banned.json"));
        templates.put("bans/unbanned", Utils.readTemplate(PATH_PREFIX + templatePack + separator + "bans" + separator + "unbanned.json"));
        templates.put("mutes/muted", Utils.readTemplate(PATH_PREFIX + templatePack + separator + "mutes" + separator + "muted.json"));
        templates.put("mutes/unmuted", Utils.readTemplate(PATH_PREFIX + templatePack + separator + "mutes" + separator + "unmuted.json"));
        templates.put("kicks/kicked", Utils.readTemplate(PATH_PREFIX + templatePack + separator + "kicks" + separator + "kicked.json"));
        templates.put("altdetects/detected", Utils.readTemplate(PATH_PREFIX + templatePack + separator + "altdetects" + separator + "detected.json"));
        templates.put("staffmode/enter-staffmode", Utils.readTemplate(PATH_PREFIX + templatePack + separator + "staffmode" + separator + "enter-staffmode.json"));
        templates.put("staffmode/exit-staffmode", Utils.readTemplate(PATH_PREFIX + templatePack + separator + "staffmode" + separator + "exit-staffmode.json"));
    }

    public String getTemplate(String key) {
        if (!templates.containsKey(key)) {
            throw new RuntimeException("No template found with key: [" + key + "]");
        }
        return templates.get(key);
    }
}
