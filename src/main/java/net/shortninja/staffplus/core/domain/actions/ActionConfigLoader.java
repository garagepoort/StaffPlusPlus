package net.shortninja.staffplus.core.domain.actions;

import net.shortninja.staffplus.core.common.exceptions.ConfigurationException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ActionConfigLoader {

    private ActionConfigLoader() {
    }

    public static List<ConfiguredAction> loadActions(List<LinkedHashMap<String, Object>> list) {
        return list.stream().map(map -> {
            if (!map.containsKey("command")) {
                throw new ConfigurationException("Invalid actions configuration. Actions should define a command");
            }
            String command = (String) map.get("command");
            String rollbackCommand = (String) map.get("rollback-command");
            String target = map.containsKey("target") ? (String) map.get("target") : "player";
            ActionRunStrategy runStrategy = map.containsKey("run-strategy") ? ActionRunStrategy.valueOf((String) map.get("run-strategy")) : ActionRunStrategy.ALWAYS;
            ActionRunStrategy rollbackRunStrategy = map.containsKey("rollback-run-strategy") ? ActionRunStrategy.valueOf((String) map.get("rollback-run-strategy")) : runStrategy;

            Map<String, String> filterMap = loadFilters(map);

            return new ConfiguredAction(command, rollbackCommand, runStrategy, rollbackRunStrategy, filterMap, target);
        }).collect(Collectors.toList());
    }

    private static Map<String, String> loadFilters(LinkedHashMap<String, Object> map) {
        String filtersString = map.containsKey("filters") ? (String) map.get("filters") : null;
        Map<String, String> filterMap = new HashMap<>();
        if (filtersString != null) {
            String[] split = filtersString.split(";");
            for (String filter : split) {
                String[] filterPair = filter.split("=");
                filterMap.put(filterPair[0], filterPair[1]);
            }
        }
        return filterMap;
    }
}
