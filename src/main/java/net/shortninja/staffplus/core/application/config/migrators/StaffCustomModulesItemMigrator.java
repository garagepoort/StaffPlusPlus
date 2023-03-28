package net.shortninja.staffplus.core.application.config.migrators;

import be.garagepoort.mcioc.configuration.files.ConfigurationFile;
import be.garagepoort.mcioc.configuration.yaml.configuration.ConfigurationSection;
import be.garagepoort.mcioc.configuration.yaml.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StaffCustomModulesItemMigrator implements StaffPlusPlusConfigMigrator {

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        migrateModules(configs, "staffmode-custom-modules", "custom-modules");
    }

    private void migrateModules(List<ConfigurationFile> configs, String identifier, String path) {
        FileConfiguration customModulesConfig = getConfig(configs, identifier);

        boolean configurationSection = customModulesConfig.isConfigurationSection("custom-modules");
        if(configurationSection) {
            ConfigurationSection modules = customModulesConfig.getConfigurationSection("custom-modules");
            List<ConfigurationSection> newModules = new ArrayList<>();
            if (modules != null) {
                modules.getKeys(false).forEach(k -> {
                    ConfigurationSection module = modules.getConfigurationSection(k);
                    if(module.get("item") instanceof String) {
                        HashMap<String, String> value = new HashMap<>();
                        value.put("type", (String) module.get("item"));
                        value.put("name", (String) module.get("name"));
                        value.put("lore", (String) module.get("lore"));
                        module.set("item", value);
                        module.set("lore", null);
                        newModules.add(module);
                    }
                });
                customModulesConfig.set("custom-modules", newModules);
            }
        }
    }
}
