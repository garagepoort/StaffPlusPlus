package net.shortninja.staffplus.core.domain.staff.warn.warnings.config;

import be.garagepoort.mcioc.configuration.ConfigObjectList;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;

import java.util.ArrayList;
import java.util.List;

public class WarningThresholdConfiguration {

    private static final String ERROR_MESSAGE = "Invalid warnings configuration. Threshold should define a score and actions";

    @ConfigProperty(value = "score", required = true, error = ERROR_MESSAGE)
    private int score;

    @ConfigProperty(value = "actions", required = true, error = ERROR_MESSAGE)
    @ConfigObjectList(ConfiguredCommand.class)
    private List<ConfiguredCommand> actions = new ArrayList<>();

    public int getScore() {
        return score;
    }

    public List<ConfiguredCommand> getActions() {
        return actions;
    }
}
