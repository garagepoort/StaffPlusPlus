package net.shortninja.staffplus.server.data.config.warning;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.util.lib.JavaUtils;
import net.shortninja.staffplus.util.lib.Sounds;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class WarningModuleLoader {
    private static FileConfiguration config = StaffPlus.get().getConfig();

    public static WarningConfiguration loadWarningModule() {
        boolean enabled = config.getBoolean("warnings-module.enabled");
        long clearInterval = config.getLong("warnings-module.clear");
        boolean showIssuer = config.getBoolean("warnings-module.show-issuer");
        Sounds sound = stringToSound(sanitize(config.getString("warnings-module.sound")));

        return new WarningConfiguration(enabled, showIssuer, sound, clearInterval, getTresholds(), getSeverityLevels());
    }

    private static Sounds stringToSound(String string) {
        Sounds sound = Sounds.ORB_PICKUP;
        boolean isValid = JavaUtils.isValidEnum(Sounds.class, string);

        if (!isValid) {
            IocContainer.getMessage().sendConsoleMessage("Invalid sound name '" + string + "'!", true);
        } else sound = Sounds.valueOf(string);

        return sound;
    }

    private static List<WarningThresholdConfiguration> getTresholds() {
        List list = config.getList("warnings-module.tresholds", new ArrayList<>());

        return (List<WarningThresholdConfiguration>) list.stream().map(o -> {
            LinkedHashMap<String, Object> map = (LinkedHashMap) o;
            if(!map.containsKey("score") || !map.containsKey("actions")) {
                throw new RuntimeException("Invalid warnings configuration. Treshold should define a score and actions");
            }
            int score = (Integer) map.get("score");
            List<WarningAction> actions = map.containsKey("actions") ? loadActions((List<LinkedHashMap<String, Object>>) map.get("actions")) : Collections.emptyList();

            return new WarningThresholdConfiguration(score, actions);
        }).collect(Collectors.toList());
    }

    private static List<WarningAction> loadActions(List<LinkedHashMap<String, Object>> list) {

        return list.stream().map(o -> {
            LinkedHashMap map = o;
            if(!map.containsKey("command")) {
                throw new RuntimeException("Invalid warnings actions configuration. Treshold actions should define a command");
            }
            String command = (String) map.get("command");
            WarningActionRunStrategy runStrategy = map.containsKey("run-strategy") ? WarningActionRunStrategy.valueOf((String) map.get("run-strategy")) : WarningActionRunStrategy.DELAY;

            return new WarningAction(runStrategy, command);
        }).collect(Collectors.toList());
    }

    private static List<WarningSeverityConfiguration> getSeverityLevels() {
        List list = config.getList("warnings-module.severity-levels", new ArrayList<>());

        return (List<WarningSeverityConfiguration>) list.stream().map(o -> {
            LinkedHashMap map = (LinkedHashMap) o;
            String name = (String) map.get("name");
            int score = (Integer) map.get("score");
            return new WarningSeverityConfiguration(name, score);
        }).collect(Collectors.toList());
    }

    private static String sanitize(String string) {
        if (string.contains(":")) {
            string = string.replace(string.substring(string.lastIndexOf(':')), "");
        }

        return string.toUpperCase();
    }
}
