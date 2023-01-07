package net.shortninja.staffplus.core.application.config.migrators;

import be.garagepoort.mcioc.configuration.files.ConfigurationFile;
import be.garagepoort.mcioc.configuration.yaml.configuration.file.FileConfiguration;

import java.util.List;

public class FlatFileStorageTypeMigrator implements StaffPlusPlusConfigMigrator {
    @Override
    public void migrate(List<ConfigurationFile> configs) {
        FileConfiguration config = getConfig(configs, "config");

        if (config.getString("storage.type", "sqlite").equalsIgnoreCase("flatfile")) {
            config.set("storage.type", "sqlite");
        }
    }
}
