package be.garagepoort.staffplusplus.trello;

import com.google.common.base.Charsets;
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
import java.util.concurrent.atomic.AtomicInteger;

public class ConfigUpdater {

    private static final String CONFIG_FILE = "config.yml";

    public static void updateConfig(StaffPlusPlusTrello staffPlus) {
        staffPlus.getLogger().info("Attempting to fix configuration file...");
        FileConfiguration config = staffPlus.getConfig();
        AtomicInteger counter = new AtomicInteger();
        loadConfig().forEach((k, v) -> {
            if (!config.contains(k, true) && !(v instanceof ConfigurationSection)) {
                config.set(k, v);
                counter.getAndIncrement();
            }
        });
        staffPlus.saveConfig();
        if (counter.get() > 0) {
            staffPlus.getLogger().info("Configuration file Fixed. [" + counter.get() + "] properties were added. Should StaffPlusPlusDiscord still have problems starting up, please compare your config with the default configuration: https://github.com/garagepoort/StaffPlusPlus/blob/master/StaffPlusPlusDiscord/src/main/resources/config.yml");
        } else {
            staffPlus.getLogger().info("Configuration file is up to date. No fix needed");
        }
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
            URL url = ConfigUpdater.class.getClassLoader().getResource(filename);
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
