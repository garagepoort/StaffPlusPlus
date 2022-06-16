package net.shortninja.staffplus.core.domain.blacklist.censors;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.blacklist.BlackListConfiguration;

@IocBean
@IocMultiProvider(BlacklistCensor.class)
public class IllegalCharactersBlacklistCensor implements BlacklistCensor {
    @ConfigProperty("blacklist-module.character")
    private String censorCharacter;

    private final BlackListConfiguration blackListConfiguration;

    public IllegalCharactersBlacklistCensor(Options options) {
        blackListConfiguration = options.blackListConfiguration;
    }

    @Override
    public String censor(String message) {
        String newMessage = message;

        for (String string : blackListConfiguration.getCensoredCharacters()) {
            newMessage = newMessage.replace(string, censorCharacter);
        }

        return newMessage;
    }

}
