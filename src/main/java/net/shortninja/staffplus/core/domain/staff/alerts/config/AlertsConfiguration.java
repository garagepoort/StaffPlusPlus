package net.shortninja.staffplus.core.domain.staff.alerts.config;

import net.shortninja.staffplus.core.common.Sounds;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.altdetect.AltDetectTrustLevel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AlertsConfiguration {
    private final boolean alertsNameNotify;
    private final boolean alertsMentionNotify;
    private final boolean alertsXrayEnabled;
    private final boolean alertsAltDetectEnabled;
    private final boolean alertsChatPhraseDetectionEnabled;
    private final List<AltDetectTrustLevel> alertsAltDetectTrustLevels;
    private final String permissionAlerts;
    private final String permissionAlertsAltDetect;
    private final String permissionMention;
    private final String permissionMentionBypass;
    private final String permissionNameChange;
    private final String permissionNameChangeBypass;
    private final String permissionChatPhraseDetection;
    private final String permissionChatPhraseDetectionBypass;
    private final String commandAlerts;
    private final Sounds alertsSound;
    private final XrayConfiguration xrayConfiguration;

    public AlertsConfiguration(boolean alertsNameNotify,
                               boolean alertsMentionNotify,
                               boolean alertsXrayEnabled,
                               boolean alertsAltDetectEnabled,
                               boolean alertsChatPhraseDetectionEnabled,
                               List<AltDetectTrustLevel> alertsAltDetectTrustLevels,
                               String permissionAlerts,
                               String permissionAlertsAltDetect,
                               String permissionMention,
                               String permissionMentionBypass,
                               String permissionNameChange,
                               String permissionNameChangeBypass,
                               String permissionChatPhraseDetection,
                               String permissionChatPhraseDetectionBypass, String commandAlerts,
                               Sounds alertsSound,
                               XrayConfiguration xrayConfiguration) {

        this.alertsNameNotify = alertsNameNotify;
        this.alertsMentionNotify = alertsMentionNotify;
        this.alertsXrayEnabled = alertsXrayEnabled;
        this.alertsAltDetectEnabled = alertsAltDetectEnabled;
        this.alertsChatPhraseDetectionEnabled = alertsChatPhraseDetectionEnabled;
        this.alertsAltDetectTrustLevels = alertsAltDetectTrustLevels;
        this.permissionAlerts = permissionAlerts;
        this.permissionAlertsAltDetect = permissionAlertsAltDetect;
        this.permissionMention = permissionMention;
        this.permissionMentionBypass = permissionMentionBypass;
        this.permissionNameChange = permissionNameChange;
        this.permissionNameChangeBypass = permissionNameChangeBypass;
        this.permissionChatPhraseDetection = permissionChatPhraseDetection;
        this.permissionChatPhraseDetectionBypass = permissionChatPhraseDetectionBypass;
        this.commandAlerts = commandAlerts;
        this.alertsSound = alertsSound;
        this.xrayConfiguration = xrayConfiguration;
    }

    public String getPermissionAlerts() {
        return permissionAlerts;
    }

    public String getPermissionAltDetect() {
        return permissionAlertsAltDetect;
    }

    public String getCommandAlerts() {
        return commandAlerts;
    }

    public Optional<Sounds> getAlertsSound() {
        return Optional.ofNullable(alertsSound);
    }

    public boolean isNameNotifyEnabled() {
        return alertsNameNotify;
    }

    public boolean isAlertsChatPhraseDetectionEnabled() {
        return alertsChatPhraseDetectionEnabled;
    }

    public boolean isMentionNotifyEnabled() {
        return alertsMentionNotify;
    }

    public boolean isXrayEnabled() {
        return alertsXrayEnabled;
    }

    public boolean isAltDetectEnabled() {
        return alertsAltDetectEnabled;
    }

    public List<AltDetectTrustLevel> getAlertsAltDetectTrustLevels() {
        return alertsAltDetectTrustLevels;
    }

    public XrayConfiguration getXrayConfiguration() {
        return xrayConfiguration;
    }

    public String getPermissionMention() {
        return permissionMention;
    }

    public String getPermissionMentionBypass() {
        return permissionMentionBypass;
    }

    public String getPermissionChatPhraseDetectionBypass() {
        return permissionChatPhraseDetectionBypass;
    }

    public String getPermissionNameChange() {
        return permissionNameChange;
    }

    public String getPermissionNameChangeBypass() {
        return permissionNameChangeBypass;
    }

    public String getPermissionChatPhraseDetection() {
        return permissionChatPhraseDetection;
    }

    public Set<String> getAllAlertsPermissions() {
        return new HashSet<>(Arrays.asList(getPermissionMention(), getPermissionNameChange(), getXrayConfiguration().getPermissionXray()));
    }

    public String getPermissionForType(AlertType alertType) {
        switch (alertType) {
            case XRAY:
                return getXrayConfiguration().getPermissionXray();
            case MENTION:
                return getPermissionMention();
            case ALT_DETECT:
                return getPermissionAltDetect();
            case NAME_CHANGE:
                return getPermissionNameChange();
            case CHAT_PHRASE_DETECTION:
                return getPermissionChatPhraseDetection();
            default:
                throw new BusinessException("&CUnsupported alertType [" + alertType + "]");
        }
    }
}
