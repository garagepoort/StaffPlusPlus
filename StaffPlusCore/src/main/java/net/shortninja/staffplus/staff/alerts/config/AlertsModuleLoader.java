package net.shortninja.staffplus.staff.alerts.config;

import net.shortninja.staffplus.common.Sounds;
import net.shortninja.staffplus.common.config.ConfigLoader;
import net.shortninja.staffplus.staff.alerts.xray.XrayBlockConfig;
import net.shortninja.staffplusplus.altdetect.AltDetectTrustLevel;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AlertsModuleLoader extends ConfigLoader<AlertsConfiguration> {
    @Override
    protected AlertsConfiguration load(FileConfiguration config) {
        boolean alertsNameNotify = config.getBoolean("alerts-module.name-notify");
        boolean alertsMentionNotify = config.getBoolean("alerts-module.mention-notify");
        boolean alertsXrayEnabled = config.getBoolean("alerts-module.xray-alerts.enabled");
        boolean alertsAltDetectEnabled = config.getBoolean("alerts-module.alt-detect-notify.enabled");
        List<AltDetectTrustLevel> alertsAltDetectTrustLevels = Arrays.stream(config.getString("alerts-module.alt-detect-notify.trust-levels", "").split(";"))
            .map(AltDetectTrustLevel::valueOf)
            .collect(Collectors.toList());

        String permissionAlerts = config.getString("permissions.alerts");
        String permissionAlertsAltDetect = config.getString("permissions.alerts-alt-detect");
        String permissionMention = config.getString("permissions.mention");
        String permissionNameChange = config.getString("permissions.name-change");
        String permissionWordMention = config.getString("permissions.alerts-word-mention");
        Sounds alertsSound = stringToSound(sanitize(config.getString("alerts-module.sound")));
        String commandAlerts = config.getString("commands.alerts");

        String permissionXray = config.getString("permissions.xray");
        List<XrayBlockConfig> alertsXrayBlocks = Arrays.stream(config.getString("alerts-module.xray-alerts.blocks").split("\\s*,\\s*"))
            .map(XrayBlockConfig::new)
            .collect(Collectors.toList());

        return new AlertsConfiguration(
            alertsNameNotify,
            alertsMentionNotify,
            alertsXrayEnabled,
            alertsAltDetectEnabled,
            alertsAltDetectTrustLevels,
            permissionAlerts,
            permissionAlertsAltDetect,
            permissionMention,
            permissionNameChange,
            permissionWordMention, commandAlerts,
            alertsSound,
            new XrayConfiguration(alertsXrayBlocks, permissionXray));
    }
}
