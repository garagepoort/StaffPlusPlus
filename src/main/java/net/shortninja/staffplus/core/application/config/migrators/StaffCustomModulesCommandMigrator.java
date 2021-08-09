package net.shortninja.staffplus.core.application.config.migrators;

import net.shortninja.staffplus.core.application.config.ConfigurationFile;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StaffCustomModulesCommandMigrator implements ConfigMigrator {

    private void migrateModule(FileConfiguration defaultConfig, FileConfiguration modulesConfig, String from, String to) {
        ConfigurationSection configurationSection = defaultConfig.getConfigurationSection(from);
        if (configurationSection != null) {
            modulesConfig.set(to, configurationSection);
        }
    }

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        FileConfiguration customModulesConfig = getConfig(configs, "staffmode-custom-modules");
        ConfigurationSection modules = customModulesConfig.getConfigurationSection("custom-modules");
        if (modules != null) {
            modules.getKeys(false).forEach(k -> {
                ConfigurationSection module = modules.getConfigurationSection(k);
                if (module.contains("command")) {
                    String command = module.getString("command");
                    if (StringUtils.isNotBlank(command)) {
                        List<String> commands = Collections.singletonList(command);
                        module.set("commands", commands);
                        module.set("command", null);
                    }
                }
            });
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
        if (modules == null) {
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
