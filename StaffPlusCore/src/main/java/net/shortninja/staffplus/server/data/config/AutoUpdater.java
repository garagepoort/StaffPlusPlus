package net.shortninja.staffplus.server.data.config;

import com.google.common.base.Charsets;
import net.shortninja.staffplus.StaffPlus;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AutoUpdater {

    private static final String CONFIG_FILE = "config.yml";

    public static void updateConfig(StaffPlus staffPlus) {
        FileConfiguration config = staffPlus.getConfig();
        loadConfig().forEach((k, v) -> {
            if (!config.contains(k, true) && !(v instanceof ConfigurationSection)) {
                config.set(k, v);
            }
        });
        staffPlus.saveConfig();
    }

    private static Map<String, Object> loadConfig() {
        Map<String, Object> configurations = new HashMap<>();
        InputStream defConfigStream = getResource(CONFIG_FILE);
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
            URL url = AutoUpdater.class.getClassLoader().getResource(filename);
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
