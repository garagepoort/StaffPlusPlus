package net.shortninja.staffplus.core.application.config.migrators;

import be.garagepoort.mcioc.configuration.files.ConfigurationFile;
import be.garagepoort.mcioc.configuration.yaml.configuration.ConfigurationSection;
import be.garagepoort.mcioc.configuration.yaml.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class StaffCustomModulesRemoveKeyMigrator implements StaffPlusPlusConfigMigrator {

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        FileConfiguration customModulesConfig = getConfig(configs, "staffmode-custom-modules");
        boolean configurationSection = customModulesConfig.isConfigurationSection("custom-modules");
        if(configurationSection) {
            ConfigurationSection modules = customModulesConfig.getConfigurationSection("custom-modules");
            List<LinkedHashMap<String, Object>> newModules = new ArrayList<>();
            if (modules != null) {
                modules.getKeys(false).forEach(k -> {
                    ConfigurationSection module = modules.getConfigurationSection(k);
                    LinkedHashMap<String, Object> map = new LinkedHashMap<>(module.getValues(false));
                    map.put("name", k);
                    newModules.add(map);
                });
            }
            customModulesConfig.set("custom-modules", newModules);
        } else {
            Object o = customModulesConfig.get("custom-modules");
            if(o == null || o instanceof String) {
                customModulesConfig.set("custom-modules", new ArrayList<>());
            }
        }
    }
}
