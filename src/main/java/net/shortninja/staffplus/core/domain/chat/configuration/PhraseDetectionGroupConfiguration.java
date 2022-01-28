package net.shortninja.staffplus.core.domain.chat.configuration;

import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;

import java.util.List;

public class PhraseDetectionGroupConfiguration {

    public final List<String> phrases;
    public final List<ConfiguredCommand> actions;

    public PhraseDetectionGroupConfiguration(List<String> phrases, List<ConfiguredCommand> actions) {
        this.phrases = phrases;
        this.actions = actions;
    }
}
