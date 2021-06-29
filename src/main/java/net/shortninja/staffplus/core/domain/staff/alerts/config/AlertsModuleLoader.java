package net.shortninja.staffplus.core.domain.staff.alerts.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.Sounds;
import net.shortninja.staffplus.core.application.config.AbstractConfigLoader;
import net.shortninja.staffplus.core.domain.staff.alerts.xray.XrayBlockConfig;
import net.shortninja.staffplusplus.altdetect.AltDetectTrustLevel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@IocBean
public class AlertsModuleLoader extends AbstractConfigLoader<AlertsConfiguration> {
    @Override
    protected AlertsConfiguration load() {
        boolean alertsNameNotify = defaultConfig.getBoolean("alerts-module.name-notify");
        boolean alertsMentionNotify = defaultConfig.getBoolean("alerts-module.mention-notify");
        boolean alertsXrayEnabled = defaultConfig.getBoolean("alerts-module.xray-alerts.enabled");
        boolean alertsAltDetectEnabled = defaultConfig.getBoolean("alerts-module.alt-detect-notify.enabled");
        boolean alertsChatPhraseDetectionEnabled = defaultConfig.getBoolean("alerts-module.chat-phrase-detection");
        List<AltDetectTrustLevel> alertsAltDetectTrustLevels = Arrays.stream(defaultConfig.getString("alerts-module.alt-detect-notify.trust-levels", "").split(";"))
            .map(AltDetectTrustLevel::valueOf)
            .collect(Collectors.toList());

        String permissionAlerts = permissionsConfig.getString("permissions.alerts");
        String permissionAlertsAltDetect = permissionsConfig.getString("permissions.alerts-alt-detect");
        String permissionMention = permissionsConfig.getString("permissions.mention");
        String permissionMentionBypass = permissionsConfig.getString("permissions.mention-bypass");
        String permissionNameChange = permissionsConfig.getString("permissions.name-change");
        String permissionNameChangeBypass = permissionsConfig.getString("permissions.name-change-bypass");
        String permissionChatPhraseDetection = permissionsConfig.getString("permissions.alerts-chat-phrase-detection");
        String permissionChatPhraseDetectionBypass = permissionsConfig.getString("permissions.alerts-chat-phrase-detection-bypass");
        Sounds alertsSound = stringToSound(sanitize(defaultConfig.getString("alerts-module.sound")));
        String commandAlerts = commandsConfig.getString("commands.alerts");

        String permissionXray = permissionsConfig.getString("permissions.xray");
        String permissionXrayBypass = permissionsConfig.getString("permissions.xray-bypass");
        List<XrayBlockConfig> alertsXrayBlocks = Arrays.stream(defaultConfig.getString("alerts-module.xray-alerts.blocks").split("\\s*,\\s*"))
            .map(XrayBlockConfig::new)
            .collect(Collectors.toList());

        return new AlertsConfiguration(
            alertsNameNotify,
            alertsMentionNotify,
            alertsXrayEnabled,
            alertsAltDetectEnabled,
            alertsChatPhraseDetectionEnabled,
            alertsAltDetectTrustLevels,
            permissionAlerts,
            permissionAlertsAltDetect,
            permissionMention,
            permissionMentionBypass,
            permissionNameChange,
            permissionNameChangeBypass,
            permissionChatPhraseDetection,
            permissionChatPhraseDetectionBypass, commandAlerts,
            alertsSound,
            new XrayConfiguration(alertsXrayBlocks, permissionXray, permissionXrayBypass));
    }
}
