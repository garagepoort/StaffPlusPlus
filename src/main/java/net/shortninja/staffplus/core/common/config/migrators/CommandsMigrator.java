package net.shortninja.staffplus.core.common.config.migrators;

import net.shortninja.staffplus.core.common.config.ConfigurationFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class CommandsMigrator implements ConfigMigrator {

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
        FileConfiguration commandsConfig = getConfig(configs, "commands");
        migrateModule(defaultConfig, commandsConfig, "commands", "commands");
    }
}
