package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplusplus.xray.XrayEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean(conditionalOnProperty = "alerts-module.xray-alerts.console=true")
@IocListener
public class XrayAlertConsoleHandler implements Listener {

    private final Messages messages;

    public XrayAlertConsoleHandler(Messages messages) {
        this.messages = messages;
    }

    @EventHandler
    public void handle(XrayEvent event) {
        String xrayMessage = messages.alertsXray
            .replace("%target%", event.getPlayer().getName())
            .replace("%count%", Integer.toString(event.getAmount()))
            .replace("%itemtype%", JavaUtils.formatTypeName(event.getType()))
            .replace("%lightlevel%", Integer.toString(event.getLightLevel()));

        StaffPlus.get().getLogger().info(xrayMessage);
    }

}
