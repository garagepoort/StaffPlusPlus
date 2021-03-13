package net.shortninja.staffplus.staff.alerts.config;

import net.shortninja.staffplus.common.Sounds;
import net.shortninja.staffplusplus.altdetect.AltDetectTrustLevel;

import java.util.Arrays;
import java.util.List;

public class AlertsConfiguration {
    private final boolean alertsNameNotify;
    private final boolean alertsMentionNotify;
    private final boolean alertsXrayEnabled;
    private final boolean alertsAltDetectEnabled;
    private final List<AltDetectTrustLevel> alertsAltDetectTrustLevels;
    private final String permissionAlerts;
    private final String permissionAlertsAltDetect;
    private final String permissionMention;
    private final String permissionNameChange;
    private final String permissionWordMention;
    private final String commandAlerts;
    private final Sounds alertsSound;
    private final XrayConfiguration xrayConfiguration;

    public AlertsConfiguration(boolean alertsNameNotify,
                               boolean alertsMentionNotify,
                               boolean alertsXrayEnabled,
                               boolean alertsAltDetectEnabled,
                               List<AltDetectTrustLevel> alertsAltDetectTrustLevels,
                               String permissionAlerts,
                               String permissionAlertsAltDetect,
                               String permissionMention,
                               String permissionNameChange,
                               String permissionWordMention, String commandAlerts, Sounds alertsSound, XrayConfiguration xrayConfiguration) {

        this.alertsNameNotify = alertsNameNotify;
        this.alertsMentionNotify = alertsMentionNotify;
        this.alertsXrayEnabled = alertsXrayEnabled;
        this.alertsAltDetectEnabled = alertsAltDetectEnabled;
        this.alertsAltDetectTrustLevels = alertsAltDetectTrustLevels;
        this.permissionAlerts = permissionAlerts;
        this.permissionAlertsAltDetect = permissionAlertsAltDetect;
        this.permissionMention = permissionMention;
        this.permissionNameChange = permissionNameChange;
        this.permissionWordMention = permissionWordMention;
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

    public Sounds getAlertsSound() {
        return alertsSound;
    }

    public boolean isNameNotifyEnabled() {
        return alertsNameNotify;
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

    public String getPermissionNameChange() {
        return permissionNameChange;
    }

    public String getPermissionWordMention() {
        return permissionWordMention;
    }

    public List<String> getAllAlertsPermissions() {
        return Arrays.asList(getPermissionMention(), getPermissionNameChange(), getXrayConfiguration().getPermissionXray());
    }
}
