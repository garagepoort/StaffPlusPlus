package net.shortninja.staffplus.core.application.config.migrators;

import net.shortninja.staffplus.core.application.config.ConfigurationFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class StaffModeModulesMigrator implements ConfigMigrator {

    private void migrateModule(FileConfiguration defaultConfig, FileConfiguration modulesConfig, String from, String to) {
        ConfigurationSection configurationSection = defaultConfig.getConfigurationSection(from);
        if (configurationSection != null) {
            modulesConfig.set(to, configurationSection);
            defaultConfig.set(from, null);
        }
    }

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        FileConfiguration defaultConfig = getConfig(configs, "config");
        FileConfiguration modulesConfig = getConfig(configs, "staffmode-modules");
        FileConfiguration customModulesConfig = getConfig(configs, "staffmode-custom-modules");

        migrateModule(defaultConfig, modulesConfig, "staff-mode.compass-module", "modules.compass-module");
        migrateModule(defaultConfig, modulesConfig, "staff-mode.random-teleport-module", "modules.random-teleport-module");
        migrateModule(defaultConfig, modulesConfig, "staff-mode.vanish-module", "modules.vanish-module");
        migrateModule(defaultConfig, modulesConfig, "staff-mode.gui-module", "modules.gui-module");
        migrateModule(defaultConfig, modulesConfig, "staff-mode.counter-module", "modules.counter-module");
        migrateModule(defaultConfig, modulesConfig, "staff-mode.freeze-module", "modules.freeze-module");
        migrateModule(defaultConfig, modulesConfig, "staff-mode.cps-module", "modules.cps-module");
        migrateModule(defaultConfig, modulesConfig, "staff-mode.examine-module", "modules.examine-module");
        migrateModule(defaultConfig, modulesConfig, "staff-mode.follow-module", "modules.follow-module");
        migrateModule(defaultConfig, customModulesConfig, "staff-mode.custom-modules", "custom-modules");
    }
}
