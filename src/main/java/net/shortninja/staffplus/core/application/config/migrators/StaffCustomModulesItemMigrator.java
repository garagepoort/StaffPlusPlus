package net.shortninja.staffplus.core.application.config.migrators;

import be.garagepoort.mcioc.configuration.files.ConfigurationFile;
import be.garagepoort.mcioc.configuration.yaml.configuration.MemorySection;
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
		List<MemorySection> modules = (List<MemorySection>) customModulesConfig.getList(path, new ArrayList<>());
		modules.forEach(section -> {
			if (section.get("item") instanceof String) {
				HashMap<String, String> value = new HashMap<>();
				value.put("type", section.getString("item"));
				value.put("name", section.getString("name"));
				value.put("lore", section.getString("lore"));
				section.set("item", value);
				section.set("lore", null);
			}
		});
		customModulesConfig.set(path, modules);
    }
}
