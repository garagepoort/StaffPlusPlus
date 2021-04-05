package net.shortninja.staffplus.core.domain.staff.investigate.config;

import net.shortninja.staffplus.core.common.gui.GuiItemConfig;
import net.shortninja.staffplus.core.domain.actions.ConfiguredAction;

import java.util.List;

public class InvestigationConfiguration {

    private final boolean enabled;
    private final boolean allowOfflineInvestigation;
    private final int maxConcurrentInvestigation;
    private final String investigatePermission;
    private final String startInvestigationCmd;
    private final String pauseInvestigationCmd;
    private final String concludeInvestigationCmd;
    private final String addNoteCmd;
    private final boolean investigatedTitleMessageEnabled;
    private final boolean investigatedChatMessageEnabled;
    private String staffNotificationPermission;
    private final List<ConfiguredAction> startInvestigationCommands;
    private final List<ConfiguredAction> concludeInvestigationCommands;
    private final List<ConfiguredAction> pauseInvestigationCommands;
    private final String commandManageInvestigationsGui;
    private final String permissionView;
    private final String linkEvidencePermission;
    private final String addNotePermission;
    private final String deleteNotePermission;
    private final String deleteNoteOthersPermission;
    private final GuiItemConfig guiItemConfig;

    public InvestigationConfiguration(boolean enabled,
                                      boolean allowOfflineInvestigation,
                                      int maxConcurrentInvestigation,
                                      String investigatePermission,
                                      String startInvestigationCmd,
                                      String pauseInvestigationCmd,
                                      String concludeInvestigationCmd,
                                      String addNoteCmd, boolean investigatedTitleMessageEnabled,
                                      boolean investigatedChatMessageEnabled,
                                      String staffNotificationPermission,
                                      List<ConfiguredAction> startInvestigationCommands,
                                      List<ConfiguredAction> concludeInvestigationCommands,
                                      List<ConfiguredAction> pauseInvestigationCommands,
                                      String commandManageInvestigationsGui,
                                      String permissionView,
                                      String linkEvidencePermission,
                                      String addNotePermission,
                                      String deleteNotePermission,
                                      String deleteNoteOthersPermission, GuiItemConfig guiItemConfig) {
        this.enabled = enabled;
        this.allowOfflineInvestigation = allowOfflineInvestigation;
        this.maxConcurrentInvestigation = maxConcurrentInvestigation;
        this.investigatePermission = investigatePermission;
        this.startInvestigationCmd = startInvestigationCmd;
        this.pauseInvestigationCmd = pauseInvestigationCmd;
        this.concludeInvestigationCmd = concludeInvestigationCmd;
        this.addNoteCmd = addNoteCmd;
        this.investigatedTitleMessageEnabled = investigatedTitleMessageEnabled;
        this.investigatedChatMessageEnabled = investigatedChatMessageEnabled;
        this.staffNotificationPermission = staffNotificationPermission;
        this.startInvestigationCommands = startInvestigationCommands;
        this.concludeInvestigationCommands = concludeInvestigationCommands;
        this.pauseInvestigationCommands = pauseInvestigationCommands;
        this.commandManageInvestigationsGui = commandManageInvestigationsGui;
        this.permissionView = permissionView;
        this.linkEvidencePermission = linkEvidencePermission;
        this.addNotePermission = addNotePermission;
        this.deleteNotePermission = deleteNotePermission;
        this.deleteNoteOthersPermission = deleteNoteOthersPermission;
        this.guiItemConfig = guiItemConfig;
    }

    public String getLinkEvidencePermission() {
        return linkEvidencePermission;
    }
    public String getAddNotePermission() {
        return addNotePermission;
    }

    public String getDeleteNotePermission() {
        return deleteNotePermission;
    }

    public String getDeleteNoteOthersPermission() {
        return deleteNoteOthersPermission;
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
    public String getAddNoteCmd() {
        return addNoteCmd;
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

    public int getMaxConcurrentInvestigation() {
        return maxConcurrentInvestigation;
    }

    public GuiItemConfig getGuiItemConfig() {
        return guiItemConfig;
    }
}
