package net.shortninja.staffplus.domain.chat.blacklist.censors;

import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.domain.chat.blacklist.BlackListConfiguration;

public class IllegalCharactersChatCensor implements ChatCensor {
    private final BlackListConfiguration blackListConfiguration;

    public IllegalCharactersChatCensor(Options options) {
        blackListConfiguration = options.blackListConfiguration;
    }

    @Override
    public String censor(String message) {
        String newMessage = message;

        for (String string : blackListConfiguration.getCensoredCharacters()) {
            newMessage = newMessage.replace(string, blackListConfiguration.getCensorCharacter());
        }

        return newMessage;
    }

}
