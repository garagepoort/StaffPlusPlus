package net.shortninja.staffplus.core.domain.commanddetection;

import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;

import java.util.List;

public class CommandDetectionGroupConfiguration {

    public final List<String> commands;
    public final List<ConfiguredCommand> actions;

    public CommandDetectionGroupConfiguration(List<String> commands, List<ConfiguredCommand> actions) {
        this.commands = commands;
        this.actions = actions;
    }
}
