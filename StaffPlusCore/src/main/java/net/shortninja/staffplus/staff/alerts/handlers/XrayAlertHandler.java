package net.shortninja.staffplus.staff.alerts.handlers;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.JavaUtils;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.xray.XrayEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class XrayAlertHandler extends AlertsHandler implements Listener {

    public XrayAlertHandler() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler
    public void handle(XrayEvent event) {
        if (!alertsConfiguration.isXrayEnabled()) {
            return;
        }

        for (Player user : getPlayersToNotify()) {
            String xrayMessage = messages.alertsXray
                .replace("%target%", event.getPlayer().getName())
                .replace("%count%", Integer.toString(event.getAmount()))
                .replace("%itemtype%", JavaUtils.formatTypeName(event.getType()))
                .replace("%lightlevel%", Integer.toString(event.getLightLevel()));

            if (event.getDuration().isPresent()) {
                xrayMessage = xrayMessage + String.format(" in %s seconds", event.getDuration().get() / 1000);
            }
            message.send(user, xrayMessage, messages.prefixGeneral, alertsConfiguration.getXrayConfiguration().getPermissionXray());
        }

    }

    @Override
    protected AlertType getType() {
        return AlertType.XRAY;
    }

    @Override
    protected String getPermission() {
        return alertsConfiguration.getXrayConfiguration().getPermissionXray();
    }
}
