package net.shortninja.staffplus.core.domain.chat.configuration;

import be.garagepoort.mcioc.configuration.IConfigTransformer;
import net.shortninja.staffplus.core.domain.actions.config.ActionConfigLoader;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ChatPhraseDetectionGroupTransformer implements IConfigTransformer<List<PhraseDetectionGroupConfiguration>, List<LinkedHashMap<String, Object>>> {

    public String permission;
    public String message;

    @Override
    public List<PhraseDetectionGroupConfiguration> mapConfig(List<LinkedHashMap<String, Object>> list) {
        return list.stream().map(map -> {
            List<String> phrases = (List<String>) map.get("phrases");
            List<ConfiguredCommand> commands = ActionConfigLoader.loadActions((List<LinkedHashMap<String, Object>>) map.get("commands"));
            return new PhraseDetectionGroupConfiguration(phrases, commands);
        }).collect(Collectors.toList());
    }
}