package net.shortninja.staffplus.server.data.config;

import com.google.common.base.Charsets;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.file.LanguageFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class AutoUpdaterLanguageFiles {

    public static void updateConfig(StaffPlus staffPlus) {
        for (String langFile : LanguageFile.LANG_FILES) {
            try {
                updateConfig(staffPlus, langFile + ".yml");
            } catch (IOException e) {
                staffPlus.getLogger().severe("Failed to update language file: " + langFile);
                e.printStackTrace();
            }
        }
    }

    private static void updateConfig(StaffPlus staffPlus, String languageFile) throws IOException {
        staffPlus.getLogger().info("Attempting to fix language file...");

        File langFile = new File(StaffPlus.get().getDataFolder() + "/lang/", languageFile);
        FileConfiguration lang = YamlConfiguration.loadConfiguration(langFile);
        AtomicInteger counter = new AtomicInteger();

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
    }

    private static Map<String, Object> loadConfig(String filename) {
        Map<String, Object> configurations = new HashMap<>();
        InputStream defConfigStream = getResource("lang/" + filename);
        if (defConfigStream != null) {
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8));
            Set<String> keys = yamlConfiguration.getKeys(true);
            keys.forEach((k) -> {
                configurations.put(k, yamlConfiguration.get(k));
            });
        }
        return configurations;
    }

    private static InputStream getResource(String filename) {
        try {
            URL url = AutoUpdaterLanguageFiles.class.getClassLoader().getResource(filename);
            if (url == null) {
                return null;
            } else {
                URLConnection connection = url.openConnection();
                connection.setUseCaches(false);
                return connection.getInputStream();
            }
        } catch (IOException var4) {
            return null;
        }
    }
}
