package net.shortninja.staffplus.domain.staff.warn.warnings.config;

import net.shortninja.staffplus.domain.actions.ConfiguredAction;

import java.util.List;

public class WarningThresholdConfiguration {

    private final int score;
    private final List<ConfiguredAction> actions;

    public WarningThresholdConfiguration(int score, List<ConfiguredAction> actions, List<ConfiguredAction> rollbackActions) {
        this.score = score;
        this.actions = actions;
    }

    public int getScore() {
        return score;
    }

    public List<ConfiguredAction> getActions() {
        return actions;
    }

}
