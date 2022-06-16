package net.shortninja.staffplus.core.domain.blacklist.censors;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.blacklist.BlackListConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@IocBean
@IocMultiProvider(BlacklistCensor.class)
public class DomainBlacklistCensor implements BlacklistCensor {
    @ConfigProperty("blacklist-module.character")
    private String censorCharacter;

    private final BlackListConfiguration blackListConfiguration;

    public DomainBlacklistCensor(Options options) {
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
            newMessage = censor(newMessage, word, censorCharacter);
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
