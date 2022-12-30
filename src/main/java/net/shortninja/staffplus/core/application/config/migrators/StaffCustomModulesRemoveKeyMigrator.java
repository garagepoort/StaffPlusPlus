package net.shortninja.staffplus.core.application.config.migrators;

import be.garagepoort.mcioc.configuration.files.ConfigurationFile;
import be.garagepoort.mcioc.configuration.yaml.configuration.ConfigurationSection;
import be.garagepoort.mcioc.configuration.yaml.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class StaffCustomModulesRemoveKeyMigrator implements StaffPlusPlusConfigMigrator {

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        FileConfiguration customModulesConfig = getConfig(configs, "staffmode-custom-modules");
        boolean configurationSection = customModulesConfig.isConfigurationSection("custom-modules");
        if(configurationSection) {
            ConfigurationSection modules = customModulesConfig.getConfigurationSection("custom-modules");
            List<ConfigurationSection> newModules = new ArrayList<>();
            if (modules != null) {
                modules.getKeys(false).forEach(k -> {
                    ConfigurationSection module = modules.getConfigurationSection(k);
                    module.set("name", k);
                    newModules.add(module);
                });
                customModulesConfig.set("custom-modules", newModules);
            }
        }
    }
}
