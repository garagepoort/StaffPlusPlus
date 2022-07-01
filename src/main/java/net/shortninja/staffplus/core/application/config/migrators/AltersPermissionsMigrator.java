package net.shortninja.staffplus.core.application.config.migrators;

import be.garagepoort.mcioc.configuration.files.ConfigurationFile;
import be.garagepoort.mcioc.configuration.yaml.configuration.file.FileConfiguration;

import java.util.List;

public class AltersPermissionsMigrator implements StaffPlusPlusConfigMigrator {

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        FileConfiguration permissions = getConfig(configs, "permissions");

            migrate(permissions, "alerts-chat-phrase-detection-bypass", "chat-phrase-detection-bypass");
            migrate(permissions, "alerts-command-detection-bypass", "command-detection-bypass");
            migrate(permissions, "alerts", "alerts.manage-alerts");
            migrate(permissions, "mention", "alerts.notifications.mention");
            migrate(permissions, "xray", "alerts.notifications.xray");
            migrate(permissions, "name-change", "alerts.notifications.name-change");
            migrate(permissions, "alerts-alt-detect", "alerts.notifications.alt-detect");
            migrate(permissions, "alerts-chat-phrase-detection", "alerts.notifications.chat-phrase-detection");
            migrate(permissions, "alerts-command-detection", "alerts.notifications.command-detection");
            migrate(permissions, "alerts-blacklist-detection", "alerts.notifications.blacklist-detection");
    }

    private void migrate( FileConfiguration permissions, String oldProperty, String newProperty) {
        if (permissions.contains(oldProperty)) {
            String value = permissions.getString(oldProperty);
            permissions.set(newProperty, value);
            permissions.set(oldProperty, null);
        }
    }
}
