package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.domain.staff.alerts.xray.bungee.XrayAlertBungeeEvent;
import net.shortninja.staffplusplus.xray.XrayEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocListener(conditionalOnProperty = "alerts-module.xray-alerts.console=true")
public class XrayAlertConsoleHandler implements Listener {

    private final XrayLogger xrayLogger;

    public XrayAlertConsoleHandler(XrayLogger xrayLogger) {
        this.xrayLogger = xrayLogger;
    }

    @EventHandler
    public void handle(XrayEvent event) {
        StaffPlus.get().getLogger().info(xrayLogger.getLogMessage(event));
    }

    @EventHandler
    public void handle(XrayAlertBungeeEvent event) {
        StaffPlus.get().getLogger().info(xrayLogger.getLogMessage(event.getXrayAlertBungeeDto()));
    }
}
