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
        plugin.saveResource("discordtemplates/reports/report-created.json", replaceTemplates);
        boolean testFile = plugin.getDataFolder().exists("discordtemplates" + separator + "template_version.yml")
        if (replaceTemplates || !testFile) {
        plugin.saveResource("discordtemplates/reports/report-rejected.json", replaceTemplates);
        plugin.saveResource("discordtemplates/reports/report-reopened.json", replaceTemplates);
        plugin.saveResource("discordtemplates/warnings/warning-created.json", replaceTemplates);
        plugin.saveResource("discordtemplates/warnings/threshold-reached.json", replaceTemplates);
        plugin.saveResource("discordtemplates/warnings/appeals/appeal-created.json", replaceTemplates);
        plugin.saveResource("discordtemplates/warnings/appeals/appeal-approved.json", replaceTemplates);
        plugin.saveResource("discordtemplates/warnings/appeals/appeal-rejected.json", replaceTemplates);
        plugin.saveResource("discordtemplates/bans/banned.json", replaceTemplates);
        plugin.saveResource("discordtemplates/bans/unbanned.json", replaceTemplates);
        plugin.saveResource("discordtemplates/mutes/muted.json", replaceTemplates);
        plugin.saveResource("discordtemplates/mutes/unmuted.json", replaceTemplates);
        plugin.saveResource("discordtemplates/kicks/kicked.json", replaceTemplates);
        plugin.saveResource("discordtemplates/altdetects/detected.json", replaceTemplates);
        plugin.saveResource("discordtemplates/staffmode/enter-staffmode.json", replaceTemplates);
        plugin.saveResource("discordtemplates/staffmode/exit-staffmode.json", replaceTemplates);
            plugin.saveResource("discordtemplates" + separator + "template_version.yml", replaceTemplates);
        }

        templates.put("reports/report-created", Utils.readTemplate(PATH_PREFIX + "reports" + separator + "report-created.json"));
        templates.put("reports/report-accepted", Utils.readTemplate(PATH_PREFIX + "reports" + separator + "report-accepted.json"));
        templates.put("reports/report-resolved", Utils.readTemplate(PATH_PREFIX + "reports" + separator + "report-resolved.json"));
        templates.put("reports/report-rejected", Utils.readTemplate(PATH_PREFIX + "reports" + separator + "report-rejected.json"));
        templates.put("reports/report-reopened", Utils.readTemplate(PATH_PREFIX + "reports" + separator + "report-reopened.json"));
        templates.put("warnings/warning-created", Utils.readTemplate(PATH_PREFIX + "warnings" + separator + "warning-created.json"));
        templates.put("warnings/threshold-reached", Utils.readTemplate(PATH_PREFIX + "warnings" + separator + "threshold-reached.json"));
        templates.put("warnings/appeals/appeal-created", Utils.readTemplate(PATH_PREFIX + "warnings" + separator + "appeals" + separator + "appeal-created.json"));
        templates.put("warnings/appeals/appeal-approved", Utils.readTemplate(PATH_PREFIX + "warnings" + separator + "appeals" + separator + "appeal-approved.json"));
        templates.put("warnings/appeals/appeal-rejected", Utils.readTemplate(PATH_PREFIX + "warnings" + separator + "appeals" + separator + "appeal-rejected.json"));
        templates.put("bans/banned", Utils.readTemplate(PATH_PREFIX + "bans" + separator + "banned.json"));
        templates.put("bans/unbanned", Utils.readTemplate(PATH_PREFIX + "bans" + separator + "unbanned.json"));
        templates.put("mutes/muted", Utils.readTemplate(PATH_PREFIX + "mutes" + separator + "muted.json"));
        templates.put("mutes/unmuted", Utils.readTemplate(PATH_PREFIX + "mutes" + separator + "unmuted.json"));
        templates.put("kicks/kicked", Utils.readTemplate(PATH_PREFIX + "kicks" + separator + "kicked.json"));
        templates.put("altdetects/detected", Utils.readTemplate(PATH_PREFIX + "altdetects" + separator + "detected.json"));
        templates.put("staffmode/enter-staffmode", Utils.readTemplate(PATH_PREFIX + "staffmode" + separator + "enter-staffmode.json"));
        templates.put("staffmode/exit-staffmode", Utils.readTemplate(PATH_PREFIX + "staffmode" + separator + "exit-staffmode.json"));
    }

    public String getTemplate(String key) {
        if (!templates.containsKey(key)) {
            throw new RuntimeException("No template found with key: [" + key + "]");
        }
        return templates.get(key);
    }
}
