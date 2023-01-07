package net.shortninja.staffplus.core.application.config.migrators;

import be.garagepoort.mcioc.configuration.files.ConfigurationFile;
import be.garagepoort.mcioc.configuration.yaml.configuration.ConfigurationSection;
import be.garagepoort.mcioc.configuration.yaml.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Set;

public class PermissionsV2Migrator implements StaffPlusPlusConfigMigrator {

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        FileConfiguration config = getConfig(configs, "permissions");

        ConfigurationSection permissions = config.getConfigurationSection("permissions");
        if (permissions != null) {
            Set<String> keys = permissions.getKeys(false);
            for (String key : keys) {
                config.set(key, permissions.get(key));
            }
            config.set("permissions", null);
        }
    }
}
