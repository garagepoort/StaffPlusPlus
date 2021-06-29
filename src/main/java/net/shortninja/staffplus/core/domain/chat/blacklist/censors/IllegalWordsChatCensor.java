package net.shortninja.staffplus.core.domain.chat.blacklist.censors;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.chat.blacklist.BlackListConfiguration;

@IocBean
@IocMultiProvider(ChatCensor.class)
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
