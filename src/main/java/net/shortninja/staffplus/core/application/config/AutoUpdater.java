package net.shortninja.staffplus.core.application.config;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.migrators.CommandsMigrator;
import net.shortninja.staffplus.core.application.config.migrators.CommandsV2Migrator;
import net.shortninja.staffplus.core.application.config.migrators.ConfigMigrator;
import net.shortninja.staffplus.core.application.config.migrators.CustomStaffModeModuleCommandMigrator;
import net.shortninja.staffplus.core.application.config.migrators.FreezeModuleMigrator;
import net.shortninja.staffplus.core.application.config.migrators.PermissionsMigrator;
import net.shortninja.staffplus.core.application.config.migrators.PermissionsV2Migrator;
import net.shortninja.staffplus.core.application.config.migrators.ReportMessagesMigrator;
import net.shortninja.staffplus.core.application.config.migrators.StaffChatChannelMigrator;
import net.shortninja.staffplus.core.application.config.migrators.StaffChatMessageFormatMigrator;
import net.shortninja.staffplus.core.application.config.migrators.StaffCustomModulesCommandMigrator;
import net.shortninja.staffplus.core.application.config.migrators.StaffModeCommandMigrator;
import net.shortninja.staffplus.core.application.config.migrators.StaffModeNewConfiguredCommandsMigrator;
import net.shortninja.staffplus.core.application.config.migrators.StaffModeModulesMigrator;
import net.shortninja.staffplus.core.application.config.migrators.StaffModesMigrator;
import net.shortninja.staffplus.core.application.config.migrators.ThresholdCommandsMigrator;
import net.shortninja.staffplus.core.application.config.migrators.WarningCommandsMigrator;
import net.shortninja.staffplus.core.common.exceptions.ConfigurationException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class AutoUpdater {

    private static final List<ConfigMigrator> MIGRATORS = Arrays.asList(
        new StaffModeCommandMigrator(),
        new StaffModeModulesMigrator(),
        new PermissionsMigrator(),
        new CommandsMigrator(),
        new StaffModesMigrator(),
        new StaffChatChannelMigrator(),
        new StaffChatMessageFormatMigrator(),
        new ReportMessagesMigrator(),
        new PermissionsV2Migrator(),
        new CommandsV2Migrator(),
        new CustomStaffModeModuleCommandMigrator(),
        new StaffCustomModulesCommandMigrator(),
        new WarningCommandsMigrator(),
        new StaffModeNewConfiguredCommandsMigrator(),
        new ThresholdCommandsMigrator(),
        new FreezeModuleMigrator());

    public static boolean updateConfig(ConfigurationFile configurationFile) {
        try {
            validateConfigFile(configurationFile.getPath());

            FileConfiguration config = configurationFile.getFileConfiguration();
            AtomicInteger counter = new AtomicInteger();
            Map<String, Object> defaultConfigMap = loadConfig(configurationFile.getPath());

            defaultConfigMap.forEach((k, v) -> {
                if (!config.contains(k, true) && !(v instanceof ConfigurationSection)) {
                    config.set(k, v);
                    counter.getAndIncrement();
                }
            });

            File file = new File(StaffPlus.get().getDataFolder() + File.separator + configurationFile.getPath());
            config.save(file);
            if (counter.get() > 0) {
                StaffPlus.get().getLogger().info("Configuration file Fixed. [" + counter.get() + "] properties were added. Should StaffPlusPlus still have problems starting up, please compare your config with the default configuration: https://github.com/garagepoort/StaffPlusPlus/blob/master/StaffPlusCore/src/main/resources/config.yml");
            }
            return true;
        } catch (InvalidConfigurationException | IOException | ConfigurationException e) {
            StaffPlus.get().getLogger().severe("Configuration file is INVALID!!! Disabling StaffPlusPlus!");
            StaffPlus.get().getLogger().severe("Full error [" + e.getMessage() + "]");
            return false;
        }
    }

    public static void runMigrations(List<ConfigurationFile> fileConfigurations) {
        try {
            MIGRATORS.forEach(m -> m.migrate(fileConfigurations));
            for (ConfigurationFile configurationFile : fileConfigurations) {
                File file = new File(StaffPlus.get().getDataFolder() + File.separator + configurationFile.getPath());
                configurationFile.getFileConfiguration().options().copyDefaults(true);
                configurationFile.getFileConfiguration().save(file);
            }
        } catch (IOException e) {
            throw new ConfigurationException("Unable to migrate configurations", e);
        }
    }

    private static Map<String, Object> loadConfig(String filename) {
        Map<String, Object> configurations = new HashMap<>();
        InputStream defConfigStream = getResource(filename);
        if (defConfigStream != null) {
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8));
            Set<String> keys = yamlConfiguration.getKeys(true);
            keys.forEach(k -> configurations.put(k, yamlConfiguration.get(k)));
        }
        return configurations;
    }

    private static void validateConfigFile(File folder, String filename) throws IOException, InvalidConfigurationException {
        File file = new File(folder, filename);
        if (!file.exists()) {
            throw new ConfigurationException("No configuration file found");
        }
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.load(file);
    }

    private static void validateConfigFile(String filename) throws IOException, InvalidConfigurationException {
        validateConfigFile(StaffPlus.get().getDataFolder(), filename);
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
