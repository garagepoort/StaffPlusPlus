package net.shortninja.staffplus.core.blacklist.censors;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.blacklist.BlackListConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@IocBean
@IocMultiProvider(BlacklistCensor.class)
public class IllegalWordsBlacklistCensor implements BlacklistCensor {
    @ConfigProperty("blacklist-module.character")
    private String censorCharacter;
    @ConfigProperty("blacklist-module.merging")
    private boolean merging;

    private final BlackListConfiguration blackListConfiguration;

    public IllegalWordsBlacklistCensor(BlackListConfiguration blackListConfiguration) {
        this.blackListConfiguration = blackListConfiguration;
    }

    @Override
    public String censor(String message) {
        List<String> censoredWords = Arrays.stream(message.split(" "))
            .filter(w -> !isBypassable(w))
            .filter(this::wordMatched)
            .collect(Collectors.toList());

        String newMessage = message;
        for (String censoredWord : censoredWords) {
            newMessage = censor(newMessage, censoredWord, censorCharacter);
        }
        return newMessage;
    }

    private boolean wordMatched(String word) {
        String wordLower = word.toLowerCase();
        if (merging) {
            return blackListConfiguration.getCensoredWords().stream().anyMatch(wordLower::contains);
        }
        return blackListConfiguration.getCensoredWords().stream().anyMatch(wordLower::equalsIgnoreCase);
    }

    private boolean isBypassable(String word) {
        return blackListConfiguration.getAllowedWords().stream().anyMatch(word::equalsIgnoreCase);
    }
}
