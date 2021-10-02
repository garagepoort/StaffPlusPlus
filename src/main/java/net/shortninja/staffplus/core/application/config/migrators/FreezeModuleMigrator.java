package net.shortninja.staffplus.core.application.config.migrators;

import net.shortninja.staffplus.core.application.config.ConfigurationFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class FreezeModuleMigrator implements ConfigMigrator {

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        FileConfiguration config = getConfig(configs, "config");
        FileConfiguration modulesConfig = getConfig(configs, "staffmode-modules");

        if (modulesConfig.contains("modules.freeze-module") && !config.contains("freeze-module")) {
            ConfigurationSection oldConfigSection = modulesConfig.getConfigurationSection("modules.freeze-module");
            config.set("freeze-module.enabled", oldConfigSection.get("enabled"));
            config.set("freeze-module.chat", oldConfigSection.get("chat"));
            config.set("freeze-module.damage", oldConfigSection.get("damage"));
            config.set("freeze-module.timer", oldConfigSection.get("timer"));
            config.set("freeze-module.sound", oldConfigSection.get("sound"));
            config.set("freeze-module.prompt", oldConfigSection.get("prompt"));
            config.set("freeze-module.prompt-title", oldConfigSection.get("prompt-title"));
            config.set("freeze-module.logout-commands", oldConfigSection.get("logout-commands"));
            config.set("freeze-module.title-message-enabled", oldConfigSection.get("title-message-enabled"));

            oldConfigSection.set("chat", null);
            oldConfigSection.set("damage", null);
            oldConfigSection.set("timer", null);
            oldConfigSection.set("sound", null);
            oldConfigSection.set("prompt", null);
            oldConfigSection.set("prompt-title", null);
            oldConfigSection.set("logout-commands", null);
            oldConfigSection.set("title-message-enabled", null);
        }
    }
}
