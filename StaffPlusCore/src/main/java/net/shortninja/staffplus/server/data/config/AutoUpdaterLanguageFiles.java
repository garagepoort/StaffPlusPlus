package net.shortninja.staffplus.server.data.config;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.exceptions.ConfigurationException;
import net.shortninja.staffplus.server.data.file.LanguageFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

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
            staffPlus.getLogger().info("Attempting to fix language file [" + languageFile + "]");
            FileConfiguration lang = getConfigFile(languageFile);
            AtomicInteger counter = new AtomicInteger();
            File langFile = new File(StaffPlus.get().getDataFolder() + "/lang/", languageFile);

            loadConfig(languageFile).forEach((k, v) -> {
                if (!lang.contains(k, true) && !(v instanceof ConfigurationSection)) {
                    lang.set(k, v);
                    counter.getAndIncrement();
                }
            });
            lang.save(langFile);
            if (counter.get() > 0) {
                staffPlus.getLogger().info("Language file " + languageFile + " Fixed. [" + counter.get() + "] properties were added.");
            } else {
                staffPlus.getLogger().info("Language file " + languageFile + " is up to date. No fix needed");
            }
            return true;
        } catch (InvalidConfigurationException | IOException | ConfigurationException e) {
            staffPlus.getLogger().severe("Language file is INVALID!!! Disabling StaffPlusPlus!");
            staffPlus.getLogger().severe("Full error [" + e.getMessage() + "]");
            return false;
        }
    }

}
