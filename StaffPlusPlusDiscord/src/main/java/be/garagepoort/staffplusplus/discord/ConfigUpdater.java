package be.garagepoort.staffplusplus.discord;

import com.google.common.base.Charsets;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.naming.ConfigurationException;
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

public class ConfigUpdater {

    private static final String CONFIG_FILE = "config.yml";

    public static boolean updateConfig(StaffPlusPlusDiscord staffPlus) {
        try {
            staffPlus.getLogger().info("Attempting to fix configuration file...");
            FileConfiguration config = getConfigFile(CONFIG_FILE);
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
            return true;
        } catch (InvalidConfigurationException | IOException | ConfigurationException e) {
            staffPlus.getLogger().severe("Configuration file is INVALID!!! Disabling StaffPlusPlus Discord!");
            staffPlus.getLogger().severe("Full error [" + e.getMessage() + "]");
            return false;
        }
    }

    private static YamlConfiguration getConfigFile(String filename) throws IOException, InvalidConfigurationException, ConfigurationException {
        File file = new File(StaffPlusPlusDiscord.get().getDataFolder(), filename);
        if (!file.exists()) {
            throw new ConfigurationException("No configuration file found");
        }
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.load(file);
        return yamlConfiguration;
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
