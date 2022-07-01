package net.shortninja.staffplus.core.application.config.migrators;

import be.garagepoort.mcioc.configuration.files.ConfigurationFile;
import be.garagepoort.mcioc.configuration.yaml.configuration.ConfigurationSection;
import be.garagepoort.mcioc.configuration.yaml.configuration.file.FileConfiguration;

import java.util.List;

public class BlacklistModuleMigrator implements StaffPlusPlusConfigMigrator {

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        FileConfiguration config = getConfig(configs, "config");

        if (config.contains("chat-module.blacklist-module") && !config.contains("blacklist-module")) {
            ConfigurationSection oldConfigSection = config.getConfigurationSection("chat-module.blacklist-module");
            config.set("blacklist-module", oldConfigSection);
            config.set("chat-module.blacklist-module", null);
        }
    }
}
