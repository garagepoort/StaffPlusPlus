package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.alerts.config.XrayConfiguration;
import net.shortninja.staffplus.core.domain.staff.alerts.xray.bungee.XrayAlertBungeeDto;
import net.shortninja.staffplus.core.domain.staff.alerts.xray.bungee.XrayAlertBungeeEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.xray.XrayEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

@IocListener(conditionalOnProperty = "alerts-module.xray-alerts.console=true")
public class XrayAlertConsoleHandler implements Listener {

    private final Messages messages;
    private final PermissionHandler permission;
    private final XrayConfiguration xrayConfiguration;
    private final PlayerManager playerManager;

    public XrayAlertConsoleHandler(Messages messages, PermissionHandler permission, XrayConfiguration xrayConfiguration, PlayerManager playerManager) {
        this.messages = messages;
        this.permission = permission;
        this.xrayConfiguration = xrayConfiguration;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void handle(XrayEvent event) {
        if (permission.has(event.getPlayer(), xrayConfiguration.permissionXrayBypass)) {
            return;
        }

        log(event.getPlayer().getName(),
            event.getAmount(),
            event.getType().name(),
            event.getLightLevel(),
            event.getDuration(),
            event.getServerName());
    }

    @EventHandler
    public void handle(XrayAlertBungeeEvent event) {
        XrayAlertBungeeDto xrayAlertBungeeDto = event.getXrayAlertBungeeDto();
        Optional<SppPlayer> sppPlayer = playerManager.getOnOrOfflinePlayer(xrayAlertBungeeDto.getPlayerUuid());
        if (sppPlayer.isPresent() && permission.has(sppPlayer.get().getOfflinePlayer(), xrayConfiguration.permissionXrayBypass)) {
            return;
        }

        log(xrayAlertBungeeDto.getPlayerName(),
            xrayAlertBungeeDto.getAmount(),
            xrayAlertBungeeDto.getType(),
            xrayAlertBungeeDto.getLightLevel(),
            xrayAlertBungeeDto.getDuration(),
            xrayAlertBungeeDto.getServerName());
    }

    private void log(String playerName, int amount, String type, int lightLevel, Optional<Long> duration, String serverName) {
        String xrayMessage = messages.alertsXray
            .replace("%target%", playerName)
            .replace("%count%", Integer.toString(amount))
            .replace("%server%", serverName)
            .replace("%itemtype%", JavaUtils.formatTypeName(type))
            .replace("%lightlevel%", Integer.toString(lightLevel));

        if (duration.isPresent()) {
            xrayMessage = xrayMessage + String.format(" in %s seconds", duration.get() / 1000);
        }
        StaffPlus.get().getLogger().info(xrayMessage);
    }
}
