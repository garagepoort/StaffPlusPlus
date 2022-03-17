package net.shortninja.staffplus.core.application.config.migrators;

import be.garagepoort.mcioc.configuration.files.ConfigMigrator;
import be.garagepoort.mcioc.configuration.files.ConfigurationFile;
import net.shortninja.staffplus.core.application.config.Messages;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.stream.Collectors;

public interface StaffPlusPlusConfigMigrator extends ConfigMigrator {

    default List<FileConfiguration> getLangFiles(List<ConfigurationFile> configs) {
        return configs.stream()
            .filter(c -> Messages.LANG_FILES.contains(c.getIdentifier()))
            .map(ConfigurationFile::getFileConfiguration)
            .collect(Collectors.toList());
    }
}
