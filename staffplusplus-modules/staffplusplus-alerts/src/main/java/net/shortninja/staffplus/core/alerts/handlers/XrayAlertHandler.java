package net.shortninja.staffplus.core.alerts.handlers;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.alerts.config.AlertsConfiguration;
import net.shortninja.staffplus.core.alerts.config.XrayConfiguration;
import net.shortninja.staffplus.core.alerts.xray.bungee.XrayAlertBungeeDto;
import net.shortninja.staffplus.core.alerts.xray.bungee.XrayAlertBungeeEvent;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.xray.XrayEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBukkitListener(conditionalOnProperty = "alerts-module.xray-alerts.enabled=true")
public class XrayAlertHandler extends AlertsHandler implements Listener {

    private final XrayConfiguration xrayConfiguration;
    private final XrayLogger xrayLogger;

    public XrayAlertHandler(AlertsConfiguration alertsConfiguration,
                            PlayerSettingsRepository playerSettingsRepository,
                            OnlineSessionsManager sessionManager,
                            PermissionHandler permission,
                            Messages messages,
                            XrayConfiguration xrayConfiguration,
                            PlayerManager playerManager,
                            XrayLogger xrayLogger) {
        super(alertsConfiguration, playerSettingsRepository, sessionManager, permission, messages, playerManager);
        this.xrayConfiguration = xrayConfiguration;
        this.xrayLogger = xrayLogger;
    }

    @EventHandler
    public void handle(XrayEvent event) {
        messages.send(getPlayersToNotify(), xrayLogger.getLogMessage(event), messages.prefixGeneral);
    }

    @EventHandler
    public void handle(XrayAlertBungeeEvent event) {
        XrayAlertBungeeDto xrayAlertBungeeDto = event.getXrayAlertBungeeDto();
        messages.send(getPlayersToNotify(), xrayLogger.getLogMessage(xrayAlertBungeeDto), messages.prefixGeneral);
    }

    @Override
    protected AlertType getType() {
        return AlertType.XRAY;
    }

    @Override
    protected String getPermission() {
        return xrayConfiguration.permissionXray;
    }
}
