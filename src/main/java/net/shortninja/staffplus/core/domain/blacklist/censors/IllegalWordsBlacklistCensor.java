package net.shortninja.staffplus.core.domain.blacklist.censors;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.blacklist.BlackListConfiguration;

@IocBean
@IocMultiProvider(BlacklistCensor.class)
public class IllegalWordsBlacklistCensor implements BlacklistCensor {
    @ConfigProperty("blacklist-module.character")
    private String censorCharacter;

    private final BlackListConfiguration blackListConfiguration;

    public IllegalWordsBlacklistCensor(Options options) {
        blackListConfiguration = options.blackListConfiguration;
    }

    @Override
    public String censor(String message) {
        String newMessage = message;

        for (String word : newMessage.split(" ")) {
            if (blackListConfiguration.getCensoredWords().contains(word.toLowerCase()) &&
                !isBypassable(word.toLowerCase())) {
                newMessage = censor(newMessage, word, censorCharacter);
            }
        }
        return newMessage;
    }

    private boolean isBypassable(String word) {
        boolean isBypassable = false;

        for (String string : blackListConfiguration.getAllowedWords()) {
            if (word.contains(string.toLowerCase())) {
                isBypassable = true;
                break;
            }
        }

        return isBypassable;
    }
}
