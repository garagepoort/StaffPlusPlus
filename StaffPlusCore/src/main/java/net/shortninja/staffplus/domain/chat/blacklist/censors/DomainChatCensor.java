package net.shortninja.staffplus.domain.chat.blacklist.censors;

import net.shortninja.staffplus.domain.chat.blacklist.BlackListConfiguration;
import net.shortninja.staffplus.common.config.Options;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DomainChatCensor implements ChatCensor {
    private final BlackListConfiguration blackListConfiguration;

    public DomainChatCensor(Options options) {
        blackListConfiguration = options.blackListConfiguration;
    }

    @Override
    public String censor(String message) {
        String newMessage = replacePeriods(message);
        String[] words = newMessage.split(Pattern.quote("."));

        List<String> wordsToCensor = Arrays.stream(words)
            .filter(this::containsDomain)
            .collect(Collectors.toList());

        for (String word : wordsToCensor) {
            newMessage = censor(newMessage, word, blackListConfiguration.getCensorCharacter());
        }

        return newMessage;
    }

    private String replacePeriods(String newMessage) {
        for (String period : blackListConfiguration.getPeriods()) {
            newMessage = newMessage.replace(period, ".");
        }
        return newMessage;
    }

    private boolean containsDomain(String word) {
        return blackListConfiguration.getCensoredDomains().stream().anyMatch(word::equalsIgnoreCase);
    }
}
