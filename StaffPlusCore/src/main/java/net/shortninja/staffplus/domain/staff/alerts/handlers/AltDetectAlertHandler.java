package net.shortninja.staffplus.domain.staff.alerts.handlers;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.altdetect.AltDetectEvent;
import net.shortninja.staffplusplus.altdetect.IAltDetectResult;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.shortninja.staffplusplus.alerts.AlertType.ALT_DETECT;

public class AltDetectAlertHandler extends AlertsHandler implements Listener {

    public AltDetectAlertHandler() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler
    public void altDetect(AltDetectEvent altDetectEvent) {
        IAltDetectResult altDetectResult = altDetectEvent.getAltDetectResult();
        if (!alertsConfiguration.isAltDetectEnabled() || !alertsConfiguration.getAlertsAltDetectTrustLevels().contains(altDetectResult.getAltDetectTrustLevel())) {
            return;
        }

        for (Player player : getPlayersToNotify()) {
            message.send(player, String.format("&CAlt account check triggered, %s and %s might be the same player. Trust [%s]",
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
        return alertsConfiguration.getPermissionAltDetect();
    }

}
