package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.alerts.config.AlertsConfiguration;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.altdetect.AltDetectEvent;
import net.shortninja.staffplusplus.altdetect.IAltDetectResult;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.shortninja.staffplusplus.alerts.AlertType.ALT_DETECT;

@IocBean
public class AltDetectAlertHandler extends AlertsHandler implements Listener {

    public AltDetectAlertHandler(AlertsConfiguration alertsConfiguration, SessionManagerImpl sessionManager, PermissionHandler permission, Messages messages) {
        super(alertsConfiguration, sessionManager, permission, messages);
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler
    public void altDetect(AltDetectEvent altDetectEvent) {
        IAltDetectResult altDetectResult = altDetectEvent.getAltDetectResult();
        if (!alertsConfiguration.alertsAltDetectEnabled || !alertsConfiguration.alertsAltDetectTrustLevels.contains(altDetectResult.getAltDetectTrustLevel())) {
            return;
        }

        for (Player player : getPlayersToNotify()) {
            messages.send(player, String.format("&CAlt account check triggered, %s and %s might be the same player. Trust [%s]",
                altDetectResult.getPlayerCheckedName(),
                altDetectResult.getPlayerMatchedName(),
                altDetectResult.getAltDetectTrustLevel()), messages.prefixGeneral);
        }
    }

    @Override
    protected AlertType getType() {
        return ALT_DETECT;
    }

    @Override
    protected String getPermission() {
        return alertsConfiguration.permissionAlertsAltDetect;
    }

}
