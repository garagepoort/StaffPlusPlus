package net.shortninja.staffplus.core.application.config.migrators;

import be.garagepoort.mcioc.configuration.files.ConfigurationFile;
import be.garagepoort.mcioc.configuration.yaml.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class StaffCustomModulesItemMigrator implements StaffPlusPlusConfigMigrator {

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        migrateModules(configs, "staffmode-custom-modules", "custom-modules");
    }

    private void migrateModules(List<ConfigurationFile> configs, String identifier, String path) {
        FileConfiguration customModulesConfig = getConfig(configs, identifier);
        List<LinkedHashMap<String, Object>> modules = (List<LinkedHashMap<String, Object>>) customModulesConfig.getList(path, new ArrayList<>());
        modules.forEach(map -> {
            if (map.get("item") instanceof String) {
                HashMap<String, String> value = new HashMap<>();
                value.put("type", (String) map.get("item"));
                value.put("name", (String) map.get("name"));
                value.put("lore", (String) map.get("lore"));
                map.put("item", value);
                map.remove("lore");
            }
        });
        customModulesConfig.set(path, modules);
    }
}
