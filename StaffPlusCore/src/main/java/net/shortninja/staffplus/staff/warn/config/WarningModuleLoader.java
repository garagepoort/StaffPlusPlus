package net.shortninja.staffplus.staff.warn.config;

import net.shortninja.staffplus.common.config.ConfigLoader;
import net.shortninja.staffplus.util.lib.Sounds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class WarningModuleLoader extends ConfigLoader<WarningConfiguration> {

    @Override
    public WarningConfiguration load() {
        boolean enabled = config.getBoolean("warnings-module.enabled");
        // seconds to milliseconds
        long clearInterval = config.getLong("warnings-module.clear") * 1000;
        boolean showIssuer = config.getBoolean("warnings-module.show-issuer");
        boolean notifyUser = config.getBoolean("warnings-module.user-notifications.enabled");
        boolean alwaysNotifyUser = config.getBoolean("warnings-module.user-notifications.always-notify");
        String myWarningsPermission = config.getString("permissions.view-my-warnings");
        String myWarningsCmd = config.getString("commands.my-warnings");
        Sounds sound = stringToSound(sanitize(config.getString("warnings-module.sound")));

        return new WarningConfiguration(enabled,
            showIssuer,
            sound,
            clearInterval,
            notifyUser, alwaysNotifyUser,
            myWarningsPermission,
            myWarningsCmd,
            getThresholds(),
            getSeverityLevels());
    }

    private List<WarningThresholdConfiguration> getThresholds() {
        List list = config.getList("warnings-module.thresholds", new ArrayList<>());

        return (List<WarningThresholdConfiguration>) list.stream().map(o -> {
            LinkedHashMap<String, Object> map = (LinkedHashMap) o;
            if (!map.containsKey("score") || !map.containsKey("actions")) {
                throw new RuntimeException("Invalid warnings configuration. Threshold should define a score and actions");
            }
            int score = (Integer) map.get("score");
            List<WarningAction> actions = map.containsKey("actions") ? loadActions((List<LinkedHashMap<String, Object>>) map.get("actions")) : Collections.emptyList();

            return new WarningThresholdConfiguration(score, actions);
        }).collect(Collectors.toList());
    }

    private List<WarningAction> loadActions(List<LinkedHashMap<String, Object>> list) {

        return list.stream().map(o -> {
            LinkedHashMap map = o;
            if (!map.containsKey("command")) {
                throw new RuntimeException("Invalid warnings actions configuration. Threshold actions should define a command");
            }
            String command = (String) map.get("command");
            WarningActionRunStrategy runStrategy = map.containsKey("run-strategy") ? WarningActionRunStrategy.valueOf((String) map.get("run-strategy")) : WarningActionRunStrategy.DELAY;

            return new WarningAction(runStrategy, command);
        }).collect(Collectors.toList());
    }

    private List<WarningSeverityConfiguration> getSeverityLevels() {
        List list = config.getList("warnings-module.severity-levels", new ArrayList<>());

        return (List<WarningSeverityConfiguration>) list.stream().map(o -> {
            LinkedHashMap map = (LinkedHashMap) o;
            String name = (String) map.get("name");
            int score = (Integer) map.get("score");
            return new WarningSeverityConfiguration(name, score);
        }).collect(Collectors.toList());
    }

}
