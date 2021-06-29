package net.shortninja.staffplus.core.domain.actions;

import be.garagepoort.mcioc.configuration.IConfigTransformer;
import net.shortninja.staffplus.core.application.config.ConfigurationUtil;
import net.shortninja.staffplus.core.common.exceptions.ConfigurationException;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class ActionConfigLoader implements IConfigTransformer<List<ConfiguredAction>, List<LinkedHashMap<String, Object>>> {

    public static List<ConfiguredAction> loadActions(List<LinkedHashMap<String, Object>> list) {
        return mapConfigAction(list);
    }

    @Override
    public List<ConfiguredAction> mapConfig(List<LinkedHashMap<String, Object>> list) {
        return mapConfigAction(list);
    }

    @NotNull
    private static List<ConfiguredAction> mapConfigAction(List<LinkedHashMap<String, Object>> list) {
        return list.stream().map(map -> {
            if (!map.containsKey("command")) {
                throw new ConfigurationException("Invalid actions configuration. Actions should define a command");
            }
            String command = (String) map.get("command");
            String rollbackCommand = (String) map.get("rollback-command");
            String target = map.containsKey("target") ? (String) map.get("target") : "player";
            ActionRunStrategy runStrategy = map.containsKey("run-strategy") ? ActionRunStrategy.valueOf((String) map.get("run-strategy")) : ActionRunStrategy.ALWAYS;
            ActionRunStrategy rollbackRunStrategy = map.containsKey("rollback-run-strategy") ? ActionRunStrategy.valueOf((String) map.get("rollback-run-strategy")) : runStrategy;

            String filtersString = map.containsKey("filters") ? (String) map.get("filters") : null;
            Map<String, String> filterMap = ConfigurationUtil.loadFilters(filtersString);

            return new ConfiguredAction(command, rollbackCommand, runStrategy, rollbackRunStrategy, filterMap, target);
        }).collect(Collectors.toList());
    }
}
