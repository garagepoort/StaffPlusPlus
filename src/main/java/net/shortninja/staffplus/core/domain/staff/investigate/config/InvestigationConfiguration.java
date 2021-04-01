package net.shortninja.staffplus.core.domain.staff.investigate.config;

import net.shortninja.staffplus.core.domain.actions.ConfiguredAction;

import java.util.List;

public class InvestigationConfiguration {

    private final boolean enabled;
    private final boolean allowOfflineInvestigation;
    private final String investigatePermission;
    private final String startInvestigationCmd;
    private final String pauseInvestigationCmd;
    private final String concludeInvestigationCmd;
    private final boolean investigatedTitleMessageEnabled;
    private final boolean investigatedChatMessageEnabled;
    private String staffNotificationPermission;
    private final List<ConfiguredAction> startInvestigationCommands;
    private final List<ConfiguredAction> concludeInvestigationCommands;
    private final List<ConfiguredAction> pauseInvestigationCommands;
    private String commandManageInvestigationsGui;
    private String permissionView;

    public InvestigationConfiguration(boolean enabled,
                                      boolean allowOfflineInvestigation, String investigatePermission,
                                      String startInvestigationCmd,
                                      String pauseInvestigationCmd,
                                      String concludeInvestigationCmd,
                                      boolean investigatedTitleMessageEnabled,
                                      boolean investigatedChatMessageEnabled,
                                      String staffNotificationPermission, List<ConfiguredAction> startInvestigationCommands, List<ConfiguredAction> concludeInvestigationCommands, List<ConfiguredAction> pauseInvestigationCommands, String commandManageInvestigationsGui, String permissionView) {
        this.enabled = enabled;
        this.allowOfflineInvestigation = allowOfflineInvestigation;
        this.investigatePermission = investigatePermission;
        this.startInvestigationCmd = startInvestigationCmd;
        this.pauseInvestigationCmd = pauseInvestigationCmd;
        this.concludeInvestigationCmd = concludeInvestigationCmd;
        this.investigatedTitleMessageEnabled = investigatedTitleMessageEnabled;
        this.investigatedChatMessageEnabled = investigatedChatMessageEnabled;
        this.staffNotificationPermission = staffNotificationPermission;
        this.startInvestigationCommands = startInvestigationCommands;
        this.concludeInvestigationCommands = concludeInvestigationCommands;
        this.pauseInvestigationCommands = pauseInvestigationCommands;
        this.commandManageInvestigationsGui = commandManageInvestigationsGui;
        this.permissionView = permissionView;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getInvestigatePermission() {
        return investigatePermission;
    }

    public String getStartInvestigationCmd() {
        return startInvestigationCmd;
    }

    public String getPauseInvestigationCmd() {
        return pauseInvestigationCmd;
    }

    public String getConcludeInvestigationCmd() {
        return concludeInvestigationCmd;
    }

    public boolean isInvestigatedTitleMessageEnabled() {
        return investigatedTitleMessageEnabled;
    }

    public boolean isInvestigatedChatMessageEnabled() {
        return investigatedChatMessageEnabled;
    }

    public String getStaffNotificationPermission() {
        return staffNotificationPermission;
    }

    public List<ConfiguredAction> getStartInvestigationActions() {
        return startInvestigationCommands;
    }

    public List<ConfiguredAction> getConcludeInvestigationCommands() {
        return concludeInvestigationCommands;
    }

    public List<ConfiguredAction> getPauseInvestigationCommands() {
        return pauseInvestigationCommands;
    }

    public String getCommandManageInvestigationsGui() {
        return commandManageInvestigationsGui;
    }

    public String getPermissionView() {
        return permissionView;
    }

    public boolean isAllowOfflineInvestigation() {
        return allowOfflineInvestigation;
    }
}
