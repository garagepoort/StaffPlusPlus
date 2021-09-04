package net.shortninja.staffplus.core.application.config.migrators;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.ConfigurationFile;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class DataFileNotesMigrator implements ConfigMigrator {

    private static final String DATA_YML = "data.yml";

    @Override
    public void migrate(List<ConfigurationFile> config) {
        try {
            File file = new File(StaffPlus.get().getDataFolder(), DATA_YML);
            if (!file.exists()) {
                return;
            }

            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            Set<String> keys = configuration.getKeys(false);
            keys.stream()
                .filter(key -> configuration.contains(key + ".notes"))
                .forEach(key -> configuration.set(key + ".notes", null));
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
