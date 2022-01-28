package net.shortninja.staffplus.core.domain.commanddetection;

import be.garagepoort.mcioc.configuration.IConfigTransformer;
import net.shortninja.staffplus.core.domain.actions.config.ActionConfigLoader;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CommandDetectionGroupTransformer implements IConfigTransformer<List<CommandDetectionGroupConfiguration>, List<LinkedHashMap<String, Object>>> {

    public String permission;
    public String message;

    @Override
    public List<CommandDetectionGroupConfiguration> mapConfig(List<LinkedHashMap<String, Object>> list) {
        return list.stream().map(map -> {
            List<String> phrases = (List<String>) map.get("commands");
            List<ConfiguredCommand> actions = ActionConfigLoader.loadActions((List<LinkedHashMap<String, Object>>) map.get("actions"));
            return new CommandDetectionGroupConfiguration(phrases, actions);
        }).collect(Collectors.toList());
    }
}