package net.shortninja.staffplus.core.domain.staff.investigate.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;

import java.util.Optional;

@IocBean
public class InvestigationConfiguration {

    @ConfigProperty("investigations-module.enabled")
    private boolean enabled;
    @ConfigProperty("investigations-module.allow-offline-investigation")
    private boolean allowOfflineInvestigation;
    @ConfigProperty("investigations-module.enforce-staff-mode")
    private boolean enforceStaffMode;
    @ConfigProperty("investigations-module.staff-mode")
    private String staffMode;
    @ConfigProperty("investigations-module.max-concurrent-investigations")
    private int maxConcurrentInvestigation = -1;

    @ConfigProperty("permissions:investigations.manage.investigate")
    private String investigatePermission;
    @ConfigProperty("permissions:investigations.manage.link-evidence")
    private String linkEvidencePermission;
    @ConfigProperty("permissions:investigations.manage.add-note")
    private String addNotePermission;
    @ConfigProperty("permissions:investigations.manage.delete-note")
    private String deleteNotePermission;
    @ConfigProperty("permissions:investigations.manage.delete-note-others")
    private String deleteNoteOthersPermission;
    @ConfigProperty("permissions:investigations.manage.notifications")
    private String staffNotificationPermission;

    @ConfigProperty("investigations-module.notifications.investigated.chat-message-enabled")
    private boolean investigatedChatMessageEnabled;

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

    public boolean isInvestigatedChatMessageEnabled() {
        return investigatedChatMessageEnabled;
    }

    public String getStaffNotificationPermission() {
        return staffNotificationPermission;
    }

    public boolean isAllowOfflineInvestigation() {
        return allowOfflineInvestigation;
    }

    public int getMaxConcurrentInvestigation() {
        return maxConcurrentInvestigation;
    }

    public boolean isEnforceStaffMode() {
        return enforceStaffMode;
    }

    public Optional<String> getStaffMode() {
        return Optional.ofNullable(staffMode);
    }
}
