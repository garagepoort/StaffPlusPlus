package net.shortninja.staffplus.server.chat.blacklist;

import net.shortninja.staffplus.common.config.ConfigLoader;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class BlackListConfigurationLoader extends ConfigLoader<BlackListConfiguration> {

    @Override
    protected BlackListConfiguration load(FileConfiguration config) {
        boolean enabled = config.getBoolean("chat-module.blacklist-module.enabled");
        boolean hoverable = config.getBoolean("chat-module.blacklist-module.hoverable");
        String censorCharacter = config.getString("chat-module.blacklist-module.character");
        List<String> censoredWords = JavaUtils.stringToList(config.getString("chat-module.blacklist-module.words"));
        List<String> censoredCharacters = JavaUtils.stringToList(config.getString("chat-module.blacklist-module.characters"));
        List<String> censoredDomains = JavaUtils.stringToList(config.getString("chat-module.blacklist-module.domains"));
        List<String> periods = JavaUtils.stringToList(config.getString("chat-module.blacklist-module.periods"));
        List<String> allowed = JavaUtils.stringToList(config.getString("chat-module.blacklist-module.allowed"));

        censoredWords = censoredWords.stream().sorted().map(String::toLowerCase).collect(toList());
        censoredDomains = periods.stream().map(String::toLowerCase).collect(toList());
        periods = censoredDomains.stream().map(String::toLowerCase).collect(toList());

        return new BlackListConfiguration(enabled, hoverable, censorCharacter, censoredWords, censoredCharacters, censoredDomains, periods, allowed);

    }

}
