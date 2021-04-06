package net.shortninja.staffplus.core.domain.staff.warn.warnings.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.Sounds;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

import net.shortninja.staffplus.core.common.exceptions.ConfigurationException;
import net.shortninja.staffplus.core.common.time.TimeUnit;
import net.shortninja.staffplus.core.domain.actions.ConfiguredAction;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.domain.actions.ActionConfigLoader.loadActions;

@IocBean
public class WarningModuleLoader extends AbstractConfigLoader<WarningConfiguration> {

    private static final String SCORE = "score";
    private static final String ACTIONS = "actions";

    @Override
    protected WarningConfiguration load() {
        boolean enabled = defaultConfig.getBoolean("warnings-module.enabled");
        boolean showIssuer = defaultConfig.getBoolean("warnings-module.show-issuer");
        boolean notifyUser = defaultConfig.getBoolean("warnings-module.user-notifications.enabled");
        boolean alwaysNotifyUser = defaultConfig.getBoolean("warnings-module.user-notifications.always-notify");
        List<ConfiguredAction> actions = loadActions((List<LinkedHashMap<String, Object>>) defaultConfig.getList("warnings-module.actions", new ArrayList<>()));
        String myWarningsPermission = defaultConfig.getString("permissions.view-my-warnings");
        String myWarningsCmd = defaultConfig.getString("commands.my-warnings");
        Sounds sound = stringToSound(sanitize(defaultConfig.getString("warnings-module.sound")));

        return new WarningConfiguration(enabled,
            showIssuer,
            sound,
            notifyUser, alwaysNotifyUser,
            myWarningsPermission,
            myWarningsCmd,
            getThresholds(defaultConfig),
            getSeverityLevels(defaultConfig),
            actions);
    }

    private List<WarningThresholdConfiguration> getThresholds(FileConfiguration config) {
        List<LinkedHashMap<String, Object>> list = (List<LinkedHashMap<String, Object>>) config.getList("warnings-module.thresholds", new ArrayList<>());

        return Objects.requireNonNull(list).stream().map(map -> {
            if (!map.containsKey(SCORE) || !map.containsKey(ACTIONS)) {
                throw new ConfigurationException("Invalid warnings configuration. Threshold should define a score and actions");
            }
            int score = (Integer) map.get(SCORE);
            List<ConfiguredAction> actions = loadActions((List<LinkedHashMap<String, Object>>) map.get(ACTIONS));
            List<ConfiguredAction> rollbackActions = map.containsKey("rollback-actions") ? loadActions((List<LinkedHashMap<String, Object>>) map.get("rollback-actions")) : Collections.emptyList();

            return new WarningThresholdConfiguration(score, actions, rollbackActions);
        }).collect(Collectors.toList());
    }

    private List<WarningSeverityConfiguration> getSeverityLevels(FileConfiguration config) {
        List<LinkedHashMap<String, Object>> list = (List<LinkedHashMap<String, Object>>) config.getList("warnings-module.severity-levels", new ArrayList<>());

        return Objects.requireNonNull(list).stream().map(map -> {
            String name = (String) map.get("name");
            int score = (Integer) map.get(SCORE);
            long expirationDuration = map.containsKey("expiresAfter") ? TimeUnit.getDuration((String) map.get("expiresAfter")) : -1;
            String reason = (String) map.get("reason");
            boolean reasonOverwriteEnabled = map.containsKey("reasonOverwriteEnabled") ? (boolean) map.get("reasonOverwriteEnabled") : false;
            return new WarningSeverityConfiguration(name, score, expirationDuration, reason, reasonOverwriteEnabled);
        }).collect(Collectors.toList());
    }

}
