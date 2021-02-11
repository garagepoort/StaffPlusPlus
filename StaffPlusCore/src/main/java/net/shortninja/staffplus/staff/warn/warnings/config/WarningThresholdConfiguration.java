package net.shortninja.staffplus.staff.warn.warnings.config;

import net.shortninja.staffplus.common.actions.ExecutableAction;

import java.util.List;

public class WarningThresholdConfiguration {

    private final int score;
    private final List<ExecutableAction> actions;

    public WarningThresholdConfiguration(int score, List<ExecutableAction> actions) {
        this.score = score;
        this.actions = actions;
    }

    public int getScore() {
        return score;
    }

    public List<ExecutableAction> getActions() {
        return actions;
    }
}
