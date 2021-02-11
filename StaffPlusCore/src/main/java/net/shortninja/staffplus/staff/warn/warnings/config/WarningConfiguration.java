package net.shortninja.staffplus.staff.warn.warnings.config;

import net.shortninja.staffplus.common.actions.ExecutableAction;
import net.shortninja.staffplus.util.lib.Sounds;

import java.util.List;

public class WarningConfiguration {

    private boolean enabled;
    private boolean showIssuer;
    private Sounds sound;
    private long clear;
    private boolean notifyUser;
    private boolean alwaysNotifyUser;
    private final String myWarningsCmd;
    private List<WarningThresholdConfiguration> thresholds;
    private List<WarningSeverityConfiguration> severityLevels;
    private final List<ExecutableAction> actions;
    private final List<ExecutableAction> rollbackActions;
    private String myWarningsPermission;


    public WarningConfiguration(boolean enabled, boolean showIssuer, Sounds sound,
                                long clear,
                                boolean notifyUser,
                                boolean alwaysNotifyUser,
                                String myWarningsPermission,
                                String myWarningsCmd,
                                List<WarningThresholdConfiguration> thresholds, List<WarningSeverityConfiguration> severityLevels, List<ExecutableAction> actions, List<ExecutableAction> rollbackActions) {
        this.enabled = enabled;
        this.showIssuer = showIssuer;
        this.sound = sound;
        this.clear = clear;
        this.notifyUser = notifyUser;
        this.alwaysNotifyUser = alwaysNotifyUser;
        this.myWarningsPermission = myWarningsPermission;
        this.myWarningsCmd = myWarningsCmd;
        this.thresholds = thresholds;
        this.severityLevels = severityLevels;
        this.actions = actions;
        this.rollbackActions = rollbackActions;
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

    public List<ExecutableAction> getActions() {
        return actions;
    }

    public List<ExecutableAction> getRollbackActions() {
        return rollbackActions;
    }
}
