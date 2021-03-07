package net.shortninja.staffplus.server.data.config;

import org.bukkit.configuration.file.FileConfiguration;

public interface ConfigMigrator {

    void migrate(FileConfiguration config);
}
