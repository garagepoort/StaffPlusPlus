package net.shortninja.staffplus.server.data.config.warning;

import net.shortninja.staffplus.util.lib.Sounds;

import java.util.List;

public class WarningConfiguration {

    private boolean enabled;
    private boolean showIssuer;
    private Sounds sound;
    private long clear;
    private List<WarningThresholdConfiguration> thresholds;
    private List<WarningSeverityConfiguration> severityLevels;


    public WarningConfiguration(boolean enabled, boolean showIssuer, Sounds sound, long clear, List<WarningThresholdConfiguration> thresholds, List<WarningSeverityConfiguration> severityLevels) {
        this.enabled = enabled;
        this.showIssuer = showIssuer;
        this.sound = sound;
        this.clear = clear;
        this.thresholds = thresholds;
        this.severityLevels = severityLevels;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isShowIssuer() {
        return showIssuer;
    }

    public Sounds getSound() {
        return sound;
    }

    public long getClear() {
        return clear;
    }

    public List<WarningThresholdConfiguration> getThresholds() {
        return thresholds;
    }

    public List<WarningSeverityConfiguration> getSeverityLevels() {
        return severityLevels;
    }
}
