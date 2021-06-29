package net.shortninja.staffplus.core.application.config.migrators;

import net.shortninja.staffplus.core.application.config.ConfigurationFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class StaffModesMigrator implements ConfigMigrator {

    private void migrateModule(FileConfiguration defaultConfig, FileConfiguration modulesConfig, String from, String to) {
        ConfigurationSection configurationSection = defaultConfig.getConfigurationSection(from);
        if (configurationSection != null) {
            modulesConfig.set(to, configurationSection);
        }
    }

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        FileConfiguration defaultConfig = getConfig(configs, "config");
        FileConfiguration modulesConfig = getConfig(configs, "staffmode-modules");
        FileConfiguration modesConfig = getConfig(configs, "staffmode-modes");
        FileConfiguration customModulesConfig = getConfig(configs, "staffmode-custom-modules");

        if (defaultConfig.contains("staff-mode")) {
            migrateModule(defaultConfig, modesConfig, "staff-mode", "modes.default");

            List<String> guiModules = new ArrayList<>();
            migrateGuiModules(guiModules, modulesConfig);
            migrateCustomGuiModules(guiModules, customModulesConfig);
            modesConfig.set("modes.default.gui", guiModules);
            ConfigurationSection customGuis = defaultConfig.getConfigurationSection("staff-mode.custom-gui");
            if(customGuis != null) {
                customGuis.getKeys(false).forEach(key -> migrateModule(defaultConfig, modesConfig, "staff-mode.custom-gui." + key, "modes." + key));
            }

            modesConfig.set("modes.default.custom-gui", null);
            defaultConfig.set("staff-mode", null);
        }
    }

    private void migrateGuiModules(List<String> guiModules, FileConfiguration modulesConfig) {
        ConfigurationSection modules = modulesConfig.getConfigurationSection("modules");
        migrateGuiModules(guiModules, modules);
    }

    private void migrateCustomGuiModules(List<String> guiModules, FileConfiguration customModulesConfig) {
        ConfigurationSection modules = customModulesConfig.getConfigurationSection("custom-modules");
        migrateGuiModules(guiModules, modules);
    }

    private void migrateGuiModules(List<String> guiModules, ConfigurationSection modules) {
        if(modules == null) {
            return;
        }

        for (String key : modules.getKeys(false)) {
            boolean enabled = modules.getConfigurationSection(key).getBoolean("enabled");
            if (enabled) {
                int slot = modules.getConfigurationSection(key).getInt("slot");
                guiModules.add(key + ":" + slot);
            }
        }
    }
}
