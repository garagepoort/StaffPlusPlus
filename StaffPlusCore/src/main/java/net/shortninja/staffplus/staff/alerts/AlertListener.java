package net.shortninja.staffplus.staff.alerts;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplusplus.altdetect.AltDetectEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AlertListener implements Listener {

    private AlertCoordinator alertCoordinator = IocContainer.getAlertCoordinator();

    public AlertListener() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler
    public void altDetect(AltDetectEvent altDetectEvent) {
        alertCoordinator.onAltDetect(altDetectEvent.getAltDetectResult());
    }
}
