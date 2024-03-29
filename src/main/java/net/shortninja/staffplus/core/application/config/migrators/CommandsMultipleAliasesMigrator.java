package net.shortninja.staffplus.core.application.config.migrators;

import be.garagepoort.mcioc.configuration.files.ConfigurationFile;
import be.garagepoort.mcioc.configuration.yaml.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class CommandsMultipleAliasesMigrator implements StaffPlusPlusConfigMigrator {

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        FileConfiguration commands = getConfig(configs, "commands");

        if (commands != null) {
            Set<String> keys = commands.getKeys(true);
            for (String key : keys) {
                Object value = commands.get(key);
                if(value instanceof String) {
                    commands.set(key, Arrays.asList(value));
                }
            }
        }
    }
}
