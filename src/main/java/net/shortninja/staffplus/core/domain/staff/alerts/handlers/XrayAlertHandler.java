package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocListener;
import be.garagepoort.staffplusplus.craftbukkit.common.json.rayzr.JSONMessage;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.JsonSenderService;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.alerts.config.AlertsConfiguration;
import net.shortninja.staffplus.core.domain.staff.alerts.config.XrayConfiguration;
import net.shortninja.staffplus.core.domain.staff.alerts.xray.bungee.XrayAlertBungeeDto;
import net.shortninja.staffplus.core.domain.staff.alerts.xray.bungee.XrayAlertBungeeEvent;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.xray.XrayEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

@IocListener
public class XrayAlertHandler extends AlertsHandler implements Listener {

    private final XrayConfiguration xrayConfiguration;
    private final XrayLogger xrayLogger;
    private final JsonSenderService jsonSenderService;

    public XrayAlertHandler(AlertsConfiguration alertsConfiguration,
                            PlayerSettingsRepository playerSettingsRepository,
                            OnlineSessionsManager sessionManager,
                            PermissionHandler permission,
                            Messages messages,
                            XrayConfiguration xrayConfiguration,
                            PlayerManager playerManager,
                            XrayLogger xrayLogger, JsonSenderService jsonSenderService) {
        super(alertsConfiguration, playerSettingsRepository, sessionManager, permission, messages, playerManager);
        this.xrayConfiguration = xrayConfiguration;
        this.xrayLogger = xrayLogger;
        this.jsonSenderService = jsonSenderService;
    }

    @EventHandler
    public void handle(XrayEvent event) {
        if (!alertsConfiguration.alertsXrayEnabled) {
            return;
        }
        if (permission.has(event.getPlayer(), xrayConfiguration.permissionXrayBypass)) {
            return;
        }

        notifyPlayers(xrayLogger.getLogMessage(event));
    }

    @EventHandler
    public void handle(XrayAlertBungeeEvent event) {
        if (!alertsConfiguration.alertsXrayEnabled) {
            return;
        }

        XrayAlertBungeeDto xrayAlertBungeeDto = event.getXrayAlertBungeeDto();
        Optional<SppPlayer> sppPlayer = playerManager.getOnOrOfflinePlayer(xrayAlertBungeeDto.getPlayerUuid());
        if (sppPlayer.isPresent() && permission.has(sppPlayer.get().getOfflinePlayer(), xrayConfiguration.permissionXrayBypass)) {
            return;
        }

        notifyPlayers(xrayLogger.getLogMessage(xrayAlertBungeeDto));
    }

    private void notifyPlayers(String xrayMessage) {
        JSONMessage jsonMessage = JavaUtils.parseJsonMessage(xrayMessage);
        jsonSenderService.send(jsonMessage, xrayConfiguration.permissionXray, getPlayersToNotify().toArray(new Player[0]));
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
