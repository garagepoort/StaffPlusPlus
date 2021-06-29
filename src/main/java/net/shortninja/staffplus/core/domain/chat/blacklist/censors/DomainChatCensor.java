package net.shortninja.staffplus.core.domain.chat.blacklist.censors;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.chat.blacklist.BlackListConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@IocBean
@IocMultiProvider(ChatCensor.class)
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
