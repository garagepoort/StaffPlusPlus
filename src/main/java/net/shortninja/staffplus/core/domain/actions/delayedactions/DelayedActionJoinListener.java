package net.shortninja.staffplus.core.domain.actions.delayedactions;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.StaffPlusPlus;
import net.shortninja.staffplus.core.common.StaffPlusPlusJoinedEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@IocBukkitListener
public class DelayedActionJoinListener implements Listener {

    private final DelayedActionService delayedActionService;

    public DelayedActionJoinListener(DelayedActionService delayedActionService) {
        this.delayedActionService = delayedActionService;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(StaffPlusPlusJoinedEvent playerJoinEvent) {
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlusPlus.get(), () -> delayedActionService.processDelayedAction(playerJoinEvent.getPlayer()));
    }
}
