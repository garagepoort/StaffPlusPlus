package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;

import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.xray.XrayEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean
public class XrayAlertHandler extends AlertsHandler implements Listener {

    public XrayAlertHandler(Options options, SessionManagerImpl sessionManager, PermissionHandler permission, Messages messages) {
        super(options, sessionManager, permission, messages);
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler
    public void handle(XrayEvent event) {
        if (!alertsConfiguration.isXrayEnabled()) {
            return;
        }
        if (permission.has(event.getPlayer(), alertsConfiguration.getXrayConfiguration().getPermissionXrayBypass())) {
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
            messages.send(user, xrayMessage, messages.prefixGeneral, alertsConfiguration.getXrayConfiguration().getPermissionXray());
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
