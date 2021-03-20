package net.shortninja.staffplus.domain.staff.warn.warnings.config;

import net.shortninja.staffplus.domain.actions.ConfiguredAction;
import net.shortninja.staffplus.common.Sounds;

import java.util.List;
import java.util.Optional;

public class WarningConfiguration {

    private boolean enabled;
    private boolean showIssuer;
    private Sounds sound;
    private boolean notifyUser;
    private boolean alwaysNotifyUser;
    private final String myWarningsCmd;
    private List<WarningThresholdConfiguration> thresholds;
    private List<WarningSeverityConfiguration> severityLevels;
    private final List<ConfiguredAction> actions;
    private String myWarningsPermission;


    public WarningConfiguration(boolean enabled, boolean showIssuer, Sounds sound,
                                boolean notifyUser,
                                boolean alwaysNotifyUser,
                                String myWarningsPermission,
                                String myWarningsCmd,
                                List<WarningThresholdConfiguration> thresholds, List<WarningSeverityConfiguration> severityLevels, List<ConfiguredAction> actions) {
        this.enabled = enabled;
        this.showIssuer = showIssuer;
        this.sound = sound;
        this.notifyUser = notifyUser;
        this.alwaysNotifyUser = alwaysNotifyUser;
        this.myWarningsPermission = myWarningsPermission;
        this.myWarningsCmd = myWarningsCmd;
        this.thresholds = thresholds;
        this.severityLevels = severityLevels;
        this.actions = actions;
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

    public boolean isAlwaysNotifyUser() {
        return alwaysNotifyUser;
    }

    public List<WarningThresholdConfiguration> getThresholds() {
        return thresholds;
    }

    public List<WarningSeverityConfiguration> getSeverityLevels() {
        return severityLevels;
    }

    public boolean isNotifyUser() {
        return notifyUser;
    }

    public String getMyWarningsPermission() {
        return myWarningsPermission;
    }

    public String getMyWarningsCmd() {
        return myWarningsCmd;
    }

    public List<ConfiguredAction> getActions() {
        return actions;
    }

    public Optional<WarningSeverityConfiguration> getSeverityConfiguration(String severityLevel) {
        return severityLevels.stream().filter(config -> config.getName().equalsIgnoreCase(severityLevel)).findFirst();
    }
}
