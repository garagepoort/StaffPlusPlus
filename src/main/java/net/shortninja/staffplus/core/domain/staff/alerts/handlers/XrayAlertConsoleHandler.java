package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.StaffPlusPlus;
import net.shortninja.staffplus.core.domain.staff.alerts.xray.bungee.XrayAlertBungeeEvent;
import net.shortninja.staffplusplus.xray.XrayEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBukkitListener(conditionalOnProperty = "alerts-module.xray-alerts.console=true")
public class XrayAlertConsoleHandler implements Listener {

    private final XrayLogger xrayLogger;

    public XrayAlertConsoleHandler(XrayLogger xrayLogger) {
        this.xrayLogger = xrayLogger;
    }

    @EventHandler
    public void handle(XrayEvent event) {
        StaffPlusPlus.get().getLogger().info(xrayLogger.getLogMessage(event));
    }

    @EventHandler
    public void handle(XrayAlertBungeeEvent event) {
        StaffPlusPlus.get().getLogger().info(xrayLogger.getLogMessage(event.getXrayAlertBungeeDto()));
    }
}
