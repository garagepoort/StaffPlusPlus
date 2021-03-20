package net.shortninja.staffplus.common.config.migrators;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class StaffModeCommandMigrator implements ConfigMigrator {
    @Override
    public void migrate(FileConfiguration config) {
        migrateCommands(config, "staff-mode.enable-commands");
        migrateCommands(config, "staff-mode.disable-commands");
    }

    private void migrateCommands(FileConfiguration config, String s) {
        Object disableCommands = config.get(s, null);
        if (disableCommands instanceof String) {
            String[] commands = ((String) disableCommands).split(",");
            List<LinkedHashMap<String, Object>> list = new ArrayList<>();
            for (String command : commands) {
                LinkedHashMap<String, Object> hashmap = new LinkedHashMap<>();
                hashmap.put("command", command.trim());
                list.add(hashmap);
            }
            config.set(s, list);
        }
    }
}
