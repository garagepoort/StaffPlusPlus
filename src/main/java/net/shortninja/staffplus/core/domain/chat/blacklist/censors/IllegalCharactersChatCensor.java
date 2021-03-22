package net.shortninja.staffplus.core.domain.chat.blacklist.censors;

import net.shortninja.staffplus.core.application.IocBean;
import net.shortninja.staffplus.core.application.IocMultiProvider;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.chat.blacklist.BlackListConfiguration;

@IocBean
@IocMultiProvider(ChatCensor.class)
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
