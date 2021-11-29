package net.shortninja.staffplus.core.application.config.migrators;

import net.shortninja.staffplus.core.application.config.ConfigurationFile;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ServerSyncBooleanMigrator implements ConfigMigrator {

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        FileConfiguration config = getConfig(configs, "config");

        migrateSync(config, "server-sync-module.ban-sync");
        migrateSync(config, "server-sync-module.report-sync");
        migrateSync(config, "server-sync-module.warning-sync");
        migrateSync(config, "server-sync-module.mute-sync");
        migrateSync(config, "server-sync-module.kick-sync");
        migrateSync(config, "server-sync-module.investigation-sync");
        migrateSync(config, "server-sync-module.notes-sync");
    }

    private void migrateSync(FileConfiguration config, String path) {
        if (isOldConfig(config, path)) {
            config.set(path, config.getBoolean(path) ? "[ALL]" : "");
        }
    }

    private boolean isOldConfig(FileConfiguration config, String s) {
        return config.getString(s, "").equals("true") || config.getString(s, "").equals("false");
    }
}
