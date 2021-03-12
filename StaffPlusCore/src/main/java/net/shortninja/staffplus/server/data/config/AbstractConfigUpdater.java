package net.shortninja.staffplus.server.data.config;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.exceptions.ConfigurationException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AbstractConfigUpdater {

    protected static Map<String, Object> loadConfig(String filename) {
        Map<String, Object> configurations = new HashMap<>();
        InputStream defConfigStream = getResource(filename);
        if (defConfigStream != null) {
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8));
            Set<String> keys = yamlConfiguration.getKeys(true);
            keys.forEach(k -> configurations.put(k, yamlConfiguration.get(k)));
        }
        return configurations;
    }

    protected static void validateConfigFile(File folder, String filename) throws IOException, InvalidConfigurationException {
        File file = new File(folder, filename);
        if (!file.exists()) {
            throw new ConfigurationException("No configuration file found");
        }
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.load(file);
    }

    protected static void validateConfigFile(String filename) throws IOException, InvalidConfigurationException {
        validateConfigFile(StaffPlus.get().getDataFolder(), filename);
    }

    protected static InputStream getResource(String filename) {
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
