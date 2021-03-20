package net.shortninja.staffplus.common.config.migrators;

import org.bukkit.configuration.file.FileConfiguration;

public interface ConfigMigrator {

    void migrate(FileConfiguration config);
}
