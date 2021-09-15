package net.shortninja.staffplus.core.domain.staff.warn.warnings.config;

import be.garagepoort.mcioc.configuration.IConfigTransformer;
import net.shortninja.staffplus.core.common.exceptions.ConfigurationException;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.domain.actions.config.ActionConfigLoader.loadActions;

public class ThresholdConfigTransformer implements IConfigTransformer<List<WarningThresholdConfiguration>, List<LinkedHashMap<String, Object>>> {

    private static final String SCORE = "score";
    private static final String ACTIONS = "actions";

    @Override
    public List<WarningThresholdConfiguration> mapConfig(List<LinkedHashMap<String, Object>> list) {
        return list.stream().map(map -> {
            if (!map.containsKey(SCORE) || !map.containsKey(ACTIONS)) {
                throw new ConfigurationException("Invalid warnings configuration. Threshold should define a score and actions");
            }
            int score = (Integer) map.get(SCORE);
            List<ConfiguredCommand> actions = loadActions((List<LinkedHashMap<String, Object>>) map.get(ACTIONS));
            List<ConfiguredCommand> rollbackActions = map.containsKey("rollback-actions") ? loadActions((List<LinkedHashMap<String, Object>>) map.get("rollback-actions")) : Collections.emptyList();

            return new WarningThresholdConfiguration(score, actions, rollbackActions);
        }).collect(Collectors.toList());
    }
}
