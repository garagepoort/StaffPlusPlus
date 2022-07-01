package net.shortninja.staffplus.core.application.config.migrators;

import be.garagepoort.mcioc.configuration.files.ConfigurationFile;
import be.garagepoort.mcioc.configuration.yaml.configuration.file.FileConfiguration;

import java.util.List;

public class SoundOrbPickupMigrator implements StaffPlusPlusConfigMigrator {

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        FileConfiguration config = getConfig(configs, "config");

        migrateSync(config, "reports-module.sound");
        migrateSync(config, "warnings-module.sound");
        migrateSync(config, "freeze-module.sound");
        migrateSync(config, "alerts-module.sound");
    }

    private void migrateSync(FileConfiguration config, String path) {
        if (isOldConfig(config, path)) {
            config.set(path, "ENTITY_EXPERIENCE_ORB_PICKUP");
        }
    }

    private boolean isOldConfig(FileConfiguration config, String path) {
        return config.getString(path, "").equals("ORB_PICKUP");
    }
}
