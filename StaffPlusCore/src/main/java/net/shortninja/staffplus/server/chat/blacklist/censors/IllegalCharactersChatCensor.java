package net.shortninja.staffplus.server.chat.blacklist.censors;

import net.shortninja.staffplus.server.chat.blacklist.BlackListConfiguration;
import net.shortninja.staffplus.server.data.config.Options;

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
