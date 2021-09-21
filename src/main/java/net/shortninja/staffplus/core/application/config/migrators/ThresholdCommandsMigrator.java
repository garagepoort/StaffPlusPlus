package net.shortninja.staffplus.core.application.config.migrators;

import net.shortninja.staffplus.core.application.config.ConfigurationFile;
import net.shortninja.staffplus.core.domain.actions.ActionRunStrategy;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.LinkedHashMap;
import java.util.List;

public class ThresholdCommandsMigrator implements ConfigMigrator {

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        FileConfiguration config = getConfig(configs, "config");

        List<LinkedHashMap<String, Object>> thresholds = (List<LinkedHashMap<String, Object>>) config.get("warnings-module.thresholds");
        if (thresholds != null) {
            thresholds.forEach(a -> {
                List<LinkedHashMap<String, Object>> actions = (List<LinkedHashMap<String, Object>>) a.get("actions");
                actions.forEach(this::migrateChannel);
            });
        }
    }

    private void migrateChannel(LinkedHashMap<String, Object> action) {
        String command = (String) action.get("command");
        action.put("command", command.replace("%player%", "%target%"));

        Object rollbackCommand = action.get("rollback-command");
        if (rollbackCommand instanceof String) {
            LinkedHashMap<String, Object> rollbackCommandConfig = new LinkedHashMap<>();
            rollbackCommandConfig.put("command", ((String) rollbackCommand).replace("%player%", "%target%"));
            action.put("rollback-command", rollbackCommandConfig);
        }

        if (action.containsKey("run-strategy")) {
            ActionRunStrategy runStrategy = ActionRunStrategy.valueOf((String) action.get("run-strategy"));
            if (runStrategy != ActionRunStrategy.ALWAYS) {
                action.put("target", "target");
                action.put("target-run-strategy", runStrategy.name());
            }
            action.remove("run-strategy");
        }
    }
}
