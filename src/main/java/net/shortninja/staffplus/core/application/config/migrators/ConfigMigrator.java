package net.shortninja.staffplus.core.application.config.migrators;

import net.shortninja.staffplus.core.application.config.ConfigurationFile;
import net.shortninja.staffplus.core.application.config.Messages;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.stream.Collectors;

public interface ConfigMigrator {

    void migrate(List<ConfigurationFile> config);

    default FileConfiguration getConfig(List<ConfigurationFile> configs, String identifier) {
        return configs.stream().filter(c -> c.getIdentifier().equalsIgnoreCase(identifier)).findFirst().map(ConfigurationFile::getFileConfiguration).orElse(null);
    }

    default List<FileConfiguration> getLangFiles(List<ConfigurationFile> configs) {
        return configs.stream()
            .filter(c -> Messages.LANG_FILES.contains(c.getIdentifier()))
            .map(ConfigurationFile::getFileConfiguration)
            .collect(Collectors.toList());
    }
}
