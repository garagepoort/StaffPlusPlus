package net.shortninja.staffplus.core.common.config;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.data.LanguageFile;
import net.shortninja.staffplus.core.common.exceptions.ConfigurationException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class AutoUpdaterLanguageFiles extends AbstractConfigUpdater {

    public static boolean updateConfig(StaffPlus staffPlus) {
        for (String langFile : LanguageFile.LANG_FILES) {
            try {
                boolean langUpdate = updateConfig(staffPlus, langFile + ".yml");
                if (!langUpdate) {
                    return langUpdate;
                }
            } catch (IOException e) {
                staffPlus.getLogger().severe("Failed to update language file: " + langFile);
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private static boolean updateConfig(StaffPlus staffPlus, String languageFile) throws IOException {
        try {
            File langDirectory = new File(StaffPlus.get().getDataFolder() + "/lang/");
            validateConfigFile(langDirectory, languageFile);

            File langFile = new File(langDirectory, languageFile);
            FileConfiguration lang = YamlConfiguration.loadConfiguration(langFile);

            AtomicInteger counter = new AtomicInteger();

            loadConfig("lang/" + languageFile).forEach((k, v) -> {
                if (!lang.contains(k, true) && !(v instanceof ConfigurationSection)) {
                    lang.set(k, v);
                    counter.getAndIncrement();
                }
            });
            lang.save(langFile);
            if (counter.get() > 0) {
                staffPlus.getLogger().info("Language file " + languageFile + " Fixed. [" + counter.get() + "] properties were added.");
            }
            return true;
        } catch (InvalidConfigurationException | IOException | ConfigurationException e) {
            staffPlus.getLogger().severe("Language file " + languageFile + " is INVALID!!! Disabling StaffPlusPlus!");
            staffPlus.getLogger().severe("Full error [" + e.getMessage() + "]");
            return false;
        }
    }

}
