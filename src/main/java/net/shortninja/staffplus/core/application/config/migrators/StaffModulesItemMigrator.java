package net.shortninja.staffplus.core.application.config.migrators;

import be.garagepoort.mcioc.configuration.files.ConfigurationFile;
import be.garagepoort.mcioc.configuration.yaml.configuration.ConfigurationSection;
import be.garagepoort.mcioc.configuration.yaml.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;

public class StaffModulesItemMigrator implements StaffPlusPlusConfigMigrator {

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        migrateModules(configs, "staffmode-modules", "modules");
    }

    private void migrateModules(List<ConfigurationFile> configs, String identifier, String path) {
        FileConfiguration customModulesConfig = getConfig(configs, identifier);
        ConfigurationSection modules = customModulesConfig.getConfigurationSection(path);
        for (String key : modules.getKeys(false)) {
            ConfigurationSection module = modules.getConfigurationSection(key);
            if (module.contains("item") && module.get("item") instanceof String) {
                HashMap<String, String> value = new HashMap<>();
                value.put("type", (String) module.get("item"));
                value.put("name", (String) module.get("name"));
                value.put("lore", (String) module.get("lore"));
                module.set("item", value);
                module.set("name", null);
                module.set("lore", null);
            }
            if (module.contains("item-off") && module.get("item-off") instanceof String) {
                HashMap<String, String> value = new HashMap<>();
                value.put("type", (String) module.get("item-off"));
                value.put("name", (String) module.get("name"));
                value.put("lore", (String) module.get("lore"));
                module.set("item-off", value);
                module.set("name", null);
                module.set("lore", null);
            }
        }
        customModulesConfig.set(path, modules);
    }
}
