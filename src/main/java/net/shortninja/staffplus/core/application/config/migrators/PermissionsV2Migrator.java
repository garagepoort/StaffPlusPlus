package net.shortninja.staffplus.core.application.config.migrators;

import net.shortninja.staffplus.core.application.config.ConfigurationFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Set;

public class PermissionsV2Migrator implements ConfigMigrator {

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        FileConfiguration config = getConfig(configs, "permissions");

        ConfigurationSection permissions = config.getConfigurationSection("permissions");
        if (permissions != null) {
            Set<String> keys = permissions.getKeys(false);
            for (String key : keys) {
                config.set(key, permissions.get(key));
            }
            config.set("permissions", null);
        }
    }
}
