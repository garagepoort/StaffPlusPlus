package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.domain.staff.alerts.config.AlertsConfiguration;
import net.shortninja.staffplusplus.altdetect.AltDetectEvent;
import net.shortninja.staffplusplus.altdetect.IAltDetectResult;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBukkitListener(conditionalOnProperty = "alerts-module.alt-detect-notify.console=true")
public class AltDetectAlertConsoleHandler implements Listener {

    private final AlertsConfiguration alertsConfiguration;

    public AltDetectAlertConsoleHandler(AlertsConfiguration alertsConfiguration) {
        this.alertsConfiguration = alertsConfiguration;
    }

    @EventHandler
    public void altDetect(AltDetectEvent altDetectEvent) {
        IAltDetectResult altDetectResult = altDetectEvent.getAltDetectResult();
        if (!alertsConfiguration.alertsAltDetectTrustLevels.contains(altDetectResult.getAltDetectTrustLevel())) {
            return;
        }

        String message = String.format("&CAlt account check triggered, %s and %s might be the same player. Trust [%s]",
            altDetectResult.getPlayerCheckedName(),
            altDetectResult.getPlayerMatchedName(),
            altDetectResult.getAltDetectTrustLevel());

        StaffPlus.get().getLogger().info(message);
    }
}
