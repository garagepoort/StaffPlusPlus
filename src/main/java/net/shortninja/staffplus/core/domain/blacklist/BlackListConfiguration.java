package net.shortninja.staffplus.core.domain.blacklist;

import java.util.List;

public class BlackListConfiguration {

    private final List<String> censoredWords;
    private final List<String> censoredCharacters;
    private final List<String> censoredDomains;
    private final List<String> periods;
    private final List<String> allowedWords;

    public BlackListConfiguration(List<String> censoredWords, List<String> censoredCharacters, List<String> censoredDomains, List<String> periods, List<String> allowedWords) {
        this.censoredWords = censoredWords;
        this.censoredCharacters = censoredCharacters;
        this.censoredDomains = censoredDomains;
        this.periods = periods;
        this.allowedWords = allowedWords;
    }

    public List<String> getCensoredWords() {
        return censoredWords;
    }

    public List<String> getCensoredCharacters() {
        return censoredCharacters;
    }

    public List<String> getCensoredDomains() {
        return censoredDomains;
    }

    public List<String> getPeriods() {
        return periods;
    }

    public List<String> getAllowedWords() {
        return allowedWords;
    }
}
