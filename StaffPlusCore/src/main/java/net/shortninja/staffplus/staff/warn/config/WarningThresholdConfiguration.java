package net.shortninja.staffplus.staff.warn.config;

import java.util.List;

public class WarningThresholdConfiguration {

    private final int score;
    private final List<WarningAction> actions;

    public WarningThresholdConfiguration(int score, List<WarningAction> actions) {
        this.score = score;
        this.actions = actions;
    }

    public int getScore() {
        return score;
    }

    public List<WarningAction> getActions() {
        return actions;
    }
}
