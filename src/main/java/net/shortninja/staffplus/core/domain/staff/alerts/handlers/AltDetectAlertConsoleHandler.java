package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.staff.alerts.config.AlertsConfiguration;
import net.shortninja.staffplusplus.altdetect.AltDetectEvent;
import net.shortninja.staffplusplus.altdetect.IAltDetectResult;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean(conditionalOnProperty = "alerts-module.alt-detect-notify.console=true")
@IocListener
public class AltDetectAlertConsoleHandler implements Listener {

    private final AlertsConfiguration alertsConfiguration;

    public AltDetectAlertConsoleHandler(Options options) {
        this.alertsConfiguration = options.alertsConfiguration;
    }

    @EventHandler
    public void altDetect(AltDetectEvent altDetectEvent) {
        IAltDetectResult altDetectResult = altDetectEvent.getAltDetectResult();
        if (!alertsConfiguration.getAlertsAltDetectTrustLevels().contains(altDetectResult.getAltDetectTrustLevel())) {
            return;
        }

        String message = String.format("&CAlt account check triggered, %s and %s might be the same player. Trust [%s]",
            altDetectResult.getPlayerCheckedName(),
            altDetectResult.getPlayerMatchedName(),
            altDetectResult.getAltDetectTrustLevel());

        StaffPlus.get().getLogger().info(message);
    }
}
