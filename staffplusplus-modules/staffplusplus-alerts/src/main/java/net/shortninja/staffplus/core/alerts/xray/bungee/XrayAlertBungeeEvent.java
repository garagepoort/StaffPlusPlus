package net.shortninja.staffplus.core.alerts.xray.bungee;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class XrayAlertBungeeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final XrayAlertBungeeDto xrayAlertBungeeDto;

    public XrayAlertBungeeEvent(XrayAlertBungeeDto xrayAlertBungeeDto) {
        this.xrayAlertBungeeDto = xrayAlertBungeeDto;
    }

    public XrayAlertBungeeDto getXrayAlertBungeeDto() {
        return xrayAlertBungeeDto;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
