package net.shortninja.staffplus.core.domain.staff.warn.warnings.config;

import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;

import java.util.List;

public class WarningThresholdConfiguration {

    private final int score;
    private final List<ConfiguredCommand> actions;

    public WarningThresholdConfiguration(int score, List<ConfiguredCommand> actions, List<ConfiguredCommand> rollbackActions) {
        this.score = score;
        this.actions = actions;
    }

    public int getScore() {
        return score;
    }

    public List<ConfiguredCommand> getActions() {
        return actions;
    }

}
