package net.shortninja.staffplus.core.domain.chat.configuration;

import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;

import java.util.List;

public class PhraseDetectionGroupConfiguration {

    public final List<String> phrases;
    public final List<ConfiguredCommand> commands;

    public PhraseDetectionGroupConfiguration(List<String> phrases, List<ConfiguredCommand> commands) {
        this.phrases = phrases;
        this.commands = commands;
    }
}
