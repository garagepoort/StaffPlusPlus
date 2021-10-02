package net.shortninja.staffplus.core.domain.actions.config;

import be.garagepoort.mcioc.configuration.IConfigTransformer;

import java.util.LinkedHashMap;
import java.util.List;

public class ConfiguredCommandsConfigTransformer implements IConfigTransformer<List<ConfiguredCommand>, List<LinkedHashMap<String, Object>>> {
    @Override
    public List<ConfiguredCommand> mapConfig(List<LinkedHashMap<String, Object>> list) {
        return ActionConfigLoader.loadActions(list);
    }
}
