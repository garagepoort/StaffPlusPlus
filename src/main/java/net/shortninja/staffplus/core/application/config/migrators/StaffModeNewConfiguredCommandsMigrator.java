package net.shortninja.staffplus.core.application.config.migrators;

import net.shortninja.staffplus.core.application.config.ConfigurationFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.LinkedHashMap;
import java.util.List;

public class StaffModeNewConfiguredCommandsMigrator implements ConfigMigrator {

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        FileConfiguration config = getConfig(configs, "staffmode-modes");

        ConfigurationSection configurationSection = config.getConfigurationSection("modes");
        if (configurationSection != null) {
            configurationSection.getKeys(false).forEach(k -> {
                List<LinkedHashMap<String, Object>> enableCommands = (List<LinkedHashMap<String, Object>>) configurationSection.get(k + ".enable-commands");
                List<LinkedHashMap<String, Object>> disableCommands = (List<LinkedHashMap<String, Object>>) configurationSection.get(k + ".disable-commands");

                if (enableCommands != null) {
                    for (LinkedHashMap<String, Object> enableCommand : enableCommands) {
                        String command = ((String) enableCommand.get("command")).replace("%player%", "%staff%");
                        enableCommand.put("command", command);
                    }
                }
                if (disableCommands != null) {
                    for (LinkedHashMap<String, Object> disableCommand : disableCommands) {
                        String command = ((String) disableCommand.get("command")).replace("%player%", "%staff%");
                        disableCommand.put("command", command);
                    }
                }
            });
        }
    }

}
