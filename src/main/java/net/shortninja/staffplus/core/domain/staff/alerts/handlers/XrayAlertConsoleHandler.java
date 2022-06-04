package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
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

    private final PermissionHandler permission;
    private final XrayConfiguration xrayConfiguration;
    private final PlayerManager playerManager;
    private final XrayLogger xrayLogger;

    public XrayAlertConsoleHandler(PermissionHandler permission,
                                   XrayConfiguration xrayConfiguration,
                                   PlayerManager playerManager,
                                   XrayLogger xrayLogger) {
        this.permission = permission;
        this.xrayConfiguration = xrayConfiguration;
        this.playerManager = playerManager;
        this.xrayLogger = xrayLogger;
    }

    @EventHandler
    public void handle(XrayEvent event) {
        if (permission.has(event.getPlayer(), xrayConfiguration.permissionXrayBypass)) {
            return;
        }

        StaffPlus.get().getLogger().info(xrayLogger.getLogMessage(event));
    }

    @EventHandler
    public void handle(XrayAlertBungeeEvent event) {
        XrayAlertBungeeDto xrayAlertBungeeDto = event.getXrayAlertBungeeDto();
        Optional<SppPlayer> sppPlayer = playerManager.getOnOrOfflinePlayer(xrayAlertBungeeDto.getPlayerUuid());
        if (sppPlayer.isPresent() && permission.has(sppPlayer.get().getOfflinePlayer(), xrayConfiguration.permissionXrayBypass)) {
            return;
        }

        StaffPlus.get().getLogger().info(xrayLogger.getLogMessage(event.getXrayAlertBungeeDto()));
    }
}
