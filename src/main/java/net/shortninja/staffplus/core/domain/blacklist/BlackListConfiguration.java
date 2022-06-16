package net.shortninja.staffplus.core.domain.blacklist;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import be.garagepoort.mcioc.configuration.transformers.ToLowerCase;
import net.shortninja.staffplus.core.application.config.SplitByComma;

import java.util.List;

@IocBean
public class BlackListConfiguration {

    @ConfigProperty("blacklist-module.words")
    @ConfigTransformer({SplitByComma.class, ToLowerCase.class})
    private List<String> censoredWords;
    @ConfigProperty("blacklist-module.characters")
    @ConfigTransformer(SplitByComma.class)
    private List<String> censoredCharacters;
    @ConfigProperty("blacklist-module.domains")
    @ConfigTransformer({SplitByComma.class, ToLowerCase.class})
    private List<String> censoredDomains;
    @ConfigProperty("blacklist-module.periods")
    @ConfigTransformer({SplitByComma.class, ToLowerCase.class})
    private List<String> periods;
    @ConfigProperty("blacklist-module.allowed")
    @ConfigTransformer(SplitByComma.class)
    private List<String> allowedWords;

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
