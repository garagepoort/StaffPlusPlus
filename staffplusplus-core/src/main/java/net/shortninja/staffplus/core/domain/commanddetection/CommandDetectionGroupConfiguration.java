package net.shortninja.staffplus.core.domain.commanddetection;

import be.garagepoort.mcioc.configuration.ConfigObjectList;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandDetectionGroupConfiguration {

    @ConfigProperty("commands")
    public List<String> commands;

    @ConfigProperty("actions")
    @ConfigObjectList(ConfiguredCommand.class)
    public List<ConfiguredCommand> actions = new ArrayList<>();
}
