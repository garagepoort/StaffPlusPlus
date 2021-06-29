package net.shortninja.staffplus.core.application.config.migrators;

import net.shortninja.staffplus.core.application.config.ConfigurationFile;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.function.Function;

public class ReportMessagesMigrator implements ConfigMigrator {
    @Override
    public void migrate(List<ConfigurationFile> config) {
        List<FileConfiguration> langFiles = getLangFiles(config);
        for (FileConfiguration langFile : langFiles) {
            replace(langFile, "reported", s -> s.replace("%target%", "%culprit%"));
            replace(langFile, "reported-staff", s -> s.replace("%player%", "%culprit%").replace("%target%", "%reporter%"));
            replace(langFile, "reports-cleared", s -> s.replace("%target%", "%culprit%"));
            replace(langFile, "reported-staff", s -> s.replace("%player%", "%culprit%").replace("%target%", "%reporter%"));

            migrate(langFile, "reports-prefix", "prefix");
            migrate(langFile, "reported", "reporter.report-created");
            migrate(langFile, "reported-staff", "report-created-notification");
            migrate(langFile, "reports-cleared", "reports-cleared");
            migrate(langFile, "reports-list-start", "reports-list-start");
            migrate(langFile, "reports-list-entry", "reports-list-entry");
            migrate(langFile, "reports-list-end", "reports-list-end");
        }
    }

    private void replace(FileConfiguration langFile, String path, Function<String, String> f) {
        if (langFile.contains(path)) {
            langFile.set(path, f.apply(langFile.getString("reports." + path)));
        }
    }

    private void migrate(FileConfiguration langFile, String path, String newPath) {
        if (langFile.contains(path)) {
            langFile.set("reports." + newPath, langFile.getString(path));
            langFile.set(path, null);
        }
    }
}
