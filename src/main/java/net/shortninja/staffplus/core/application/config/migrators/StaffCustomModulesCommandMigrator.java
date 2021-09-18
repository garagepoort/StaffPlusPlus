package net.shortninja.staffplus.core.application.config.migrators;

import net.shortninja.staffplus.core.application.config.ConfigurationFile;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;
import java.util.List;

public class StaffCustomModulesCommandMigrator implements ConfigMigrator {

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        FileConfiguration customModulesConfig = getConfig(configs, "staffmode-custom-modules");
        ConfigurationSection modules = customModulesConfig.getConfigurationSection("custom-modules");
        if (modules != null) {
            modules.getKeys(false).forEach(k -> {
                ConfigurationSection module = modules.getConfigurationSection(k);
                if (module.contains("command")) {
                    String command = module.getString("command");
                    if (StringUtils.isNotBlank(command)) {
                        List<String> commands = Collections.singletonList(command);
                        module.set("commands", commands);
                        module.set("command", null);
                    }
                }
            });
        }
    }
}
