package net.shortninja.staffplus.core.domain.staff.alerts.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import net.shortninja.staffplus.core.application.config.SoundsConfigTransformer;
import net.shortninja.staffplus.core.common.Sounds;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.altdetect.AltDetectTrustLevel;

import java.util.List;

@IocBean
public class AlertsConfiguration {

    @ConfigProperty("alerts-module.name-notify")
    public boolean alertsNameNotify;
    @ConfigProperty("alerts-module.mention-notify")
    public boolean alertsMentionNotify;
    @ConfigProperty("alerts-module.xray-alerts.enabled")
    public boolean alertsXrayEnabled;
    @ConfigProperty("alerts-module.alt-detect-notify.enabled")
    public boolean alertsAltDetectEnabled;
    @ConfigProperty("alerts-module.chat-phrase-detection")
    public boolean alertsChatPhraseDetectionEnabled;
    @ConfigProperty("alerts-module.alt-detect-notify.trust-levels")
    @ConfigTransformer(AltDetectTrustLevelConfigTransformer.class)
    public List<AltDetectTrustLevel> alertsAltDetectTrustLevels;

    @ConfigProperty("permissions:alerts")
    public String permissionAlerts;
    @ConfigProperty("permissions:alerts-alt-detect")
    public String permissionAlertsAltDetect;
    @ConfigProperty("permissions:mention")
    public String permissionMention;
    @ConfigProperty("permissions:mention-bypass")
    public String permissionMentionBypass;
    @ConfigProperty("permissions:name-change")
    public String permissionNameChange;
    @ConfigProperty("permissions:name-change-bypass")
    public String permissionNameChangeBypass;
    @ConfigProperty("permissions:alerts-chat-phrase-detection")
    public String permissionChatPhraseDetection;
    @ConfigProperty("permissions:alerts-chat-phrase-detection-bypass")
    public String permissionChatPhraseDetectionBypass;

    @ConfigProperty("commands:alerts")
    public String commandAlerts;
    @ConfigProperty("alerts-module.sound")
    @ConfigTransformer(SoundsConfigTransformer.class)
    public Sounds alertsSound;

    private final XrayConfiguration xrayConfiguration;

    public AlertsConfiguration(XrayConfiguration xrayConfiguration) {
        this.xrayConfiguration = xrayConfiguration;
    }

    public String getPermissionForType(AlertType alertType) {
        switch (alertType) {
            case XRAY:
                return xrayConfiguration.permissionXray;
            case MENTION:
                return permissionMention;
            case ALT_DETECT:
                return permissionAlertsAltDetect;
            case NAME_CHANGE:
                return permissionNameChange;
            case CHAT_PHRASE_DETECTION:
                return permissionChatPhraseDetection;
            default:
                throw new BusinessException("&CUnsupported alertType [" + alertType + "]");
        }
    }
}
