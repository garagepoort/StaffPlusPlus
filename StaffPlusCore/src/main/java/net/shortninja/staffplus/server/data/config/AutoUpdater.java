package net.shortninja.staffplus.server.data.config;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.exceptions.ConfigurationException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AutoUpdater extends AbstractConfigUpdater{

    private static final String CONFIG_FILE = "config.yml";

    private static final List<ConfigMigrator> MIGRATORS = Collections.singletonList(new StaffModeCommandMigrator());

    public static boolean updateConfig(StaffPlus staffPlus) {
        try {
            staffPlus.getLogger().info("Attempting to fix configuration file...");
            validateConfigFile(CONFIG_FILE);

            FileConfiguration config = staffPlus.getConfig();
            AtomicInteger counter = new AtomicInteger();
            Map<String, Object> defaultConfigMap = loadConfig(CONFIG_FILE);

            defaultConfigMap.forEach((k, v) -> {
                if (!config.contains(k, true) && !(v instanceof ConfigurationSection)) {
                    config.set(k, v);
                    counter.getAndIncrement();
                }
            });
            MIGRATORS.forEach(m -> m.migrate(config));

            staffPlus.saveConfig();
            if (counter.get() > 0) {
                staffPlus.getLogger().info("Configuration file Fixed. [" + counter.get() + "] properties were added. Should StaffPlusPlus still have problems starting up, please compare your config with the default configuration: https://github.com/garagepoort/StaffPlusPlus/blob/master/StaffPlusCore/src/main/resources/config.yml");
            } else {
                staffPlus.getLogger().info("Configuration file is up to date. No fix needed");
            }
            return true;
        } catch (InvalidConfigurationException | IOException | ConfigurationException e) {
            staffPlus.getLogger().severe("Configuration file is INVALID!!! Disabling StaffPlusPlus!");
            staffPlus.getLogger().severe("Full error [" + e.getMessage() + "]");
            return false;
        }
    }

}
