package net.shortninja.staffplus.core.application.config.migrators;

import net.shortninja.staffplus.core.application.config.ConfigurationFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

import static java.util.Collections.singletonList;

public class CustomStaffModeModuleCommandMigrator implements ConfigMigrator {

    private static final String CUSTOM_MODULES = "custom-modules";

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        FileConfiguration config = getConfig(configs, "staffmode-custom-modules");
        migrateCommands(config);
    }

    private void migrateCommands(FileConfiguration config) {
        ConfigurationSection configurationSection = config.getConfigurationSection(CUSTOM_MODULES);
        if (configurationSection != null) {
            for (String key : configurationSection.getKeys(false)) {
                if (config.contains(CUSTOM_MODULES + key + ".command")) {
                    String action = config.getString(CUSTOM_MODULES + key + ".command");
                    if (action != null) {
                        config.set(CUSTOM_MODULES + key + ".commands", singletonList(action));
                    }
                    config.set(CUSTOM_MODULES + key + ".command", null);
                }
            }
        }
    }
}
