package net.shortninja.staffplus.core.domain.chat.blacklist;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.AbstractConfigLoader;
import net.shortninja.staffplus.core.common.JavaUtils;

import java.util.List;

import static java.util.stream.Collectors.toList;

@IocBean
public class BlackListConfigurationLoader extends AbstractConfigLoader<BlackListConfiguration> {

    @Override
    protected BlackListConfiguration load() {
        boolean enabled = defaultConfig.getBoolean("chat-module.blacklist-module.enabled");
        boolean hoverable = defaultConfig.getBoolean("chat-module.blacklist-module.hoverable");
        String censorCharacter = defaultConfig.getString("chat-module.blacklist-module.character");
        List<String> censoredWords = JavaUtils.stringToList(defaultConfig.getString("chat-module.blacklist-module.words"));
        List<String> censoredCharacters = JavaUtils.stringToList(defaultConfig.getString("chat-module.blacklist-module.characters"));
        List<String> censoredDomains = JavaUtils.stringToList(defaultConfig.getString("chat-module.blacklist-module.domains"));
        List<String> periods = JavaUtils.stringToList(defaultConfig.getString("chat-module.blacklist-module.periods"));
        List<String> allowed = JavaUtils.stringToList(defaultConfig.getString("chat-module.blacklist-module.allowed"));

        censoredWords = censoredWords.stream().sorted().map(String::toLowerCase).collect(toList());
        censoredDomains = periods.stream().map(String::toLowerCase).collect(toList());
        periods = censoredDomains.stream().map(String::toLowerCase).collect(toList());

        return new BlackListConfiguration(enabled, hoverable, censorCharacter, censoredWords, censoredCharacters, censoredDomains, periods, allowed);

    }

}
