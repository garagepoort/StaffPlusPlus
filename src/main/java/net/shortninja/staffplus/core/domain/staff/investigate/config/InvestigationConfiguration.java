package net.shortninja.staffplus.core.domain.staff.investigate.config;

import net.shortninja.staffplus.core.common.gui.GuiItemConfig;
import net.shortninja.staffplus.core.domain.actions.ConfiguredCommand;

import java.util.List;
import java.util.Optional;

public class InvestigationConfiguration {

    private final boolean enabled;
    private final boolean allowOfflineInvestigation;
    private final boolean automaticPause;
    private final boolean enforceStaffMode;
    private final String staffMode;
    private final int maxConcurrentInvestigation;
    private final String investigatePermission;
    private final String startInvestigationCmd;
    private final String pauseInvestigationCmd;
    private final String concludeInvestigationCmd;
    private final String addNoteCmd;
    private final boolean investigatedTitleMessageEnabled;
    private final boolean investigatedChatMessageEnabled;
    private String staffNotificationPermission;
    private final List<ConfiguredCommand> startInvestigationCommands;
    private final List<ConfiguredCommand> concludeInvestigationCommands;
    private final List<ConfiguredCommand> pauseInvestigationCommands;
    private final String commandManageInvestigationsGui;
    private final String permissionView;
    private final String linkEvidencePermission;
    private final String addNotePermission;
    private final String deleteNotePermission;
    private final String deleteNoteOthersPermission;
    private final GuiItemConfig guiItemConfig;

    public InvestigationConfiguration(boolean enabled,
                                      boolean allowOfflineInvestigation,
                                      boolean automaticPause,
                                      boolean enforceStaffMode,
                                      String staffMode,
                                      int maxConcurrentInvestigation,
                                      String investigatePermission,
                                      String startInvestigationCmd,
                                      String pauseInvestigationCmd,
                                      String concludeInvestigationCmd,
                                      String addNoteCmd, boolean investigatedTitleMessageEnabled,
                                      boolean investigatedChatMessageEnabled,
                                      String staffNotificationPermission,
                                      List<ConfiguredCommand> startInvestigationCommands,
                                      List<ConfiguredCommand> concludeInvestigationCommands,
                                      List<ConfiguredCommand> pauseInvestigationCommands,
                                      String commandManageInvestigationsGui,
                                      String permissionView,
                                      String linkEvidencePermission,
                                      String addNotePermission,
                                      String deleteNotePermission,
                                      String deleteNoteOthersPermission, GuiItemConfig guiItemConfig) {
        this.enabled = enabled;
        this.allowOfflineInvestigation = allowOfflineInvestigation;
        this.automaticPause = automaticPause;
        this.enforceStaffMode = enforceStaffMode;
        this.staffMode = staffMode;
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

    public List<ConfiguredCommand> getStartInvestigationActions() {
        return startInvestigationCommands;
    }

    public List<ConfiguredCommand> getConcludeInvestigationCommands() {
        return concludeInvestigationCommands;
    }

    public List<ConfiguredCommand> getPauseInvestigationCommands() {
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

    public boolean isAutomaticPause() {
        return automaticPause;
    }

    public boolean isEnforceStaffMode() {
        return enforceStaffMode;
    }

    public Optional<String> getStaffMode() {
        return Optional.ofNullable(staffMode);
    }
}
