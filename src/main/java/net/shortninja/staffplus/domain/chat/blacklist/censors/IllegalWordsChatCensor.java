package net.shortninja.staffplus.domain.chat.blacklist.censors;

import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.domain.chat.blacklist.BlackListConfiguration;

public class IllegalWordsChatCensor implements ChatCensor {
    private final BlackListConfiguration blackListConfiguration;

    public IllegalWordsChatCensor(Options options) {
        blackListConfiguration = options.blackListConfiguration;
    }

    @Override
    public String censor(String message) {
        String newMessage = message;

        for (String word : newMessage.split(" ")) {
            if (blackListConfiguration.getCensoredWords().contains(word.toLowerCase()) &&
                !isBypassable(word.toLowerCase())) {
                newMessage = censor(newMessage, word, blackListConfiguration.getCensorCharacter());
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
