package net.shortninja.staffplus.server.data.config.warning;

import java.util.List;

public class WarningThresholdConfiguration {

    private int score;
    private List<WarningAction> actions;

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
