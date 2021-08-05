package net.shortninja.staffplus.core.domain.delayedactions;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@IocBean
@IocListener
public class DelayedActionJoinListener implements Listener {

    private final DelayedActionService delayedActionService;

    public DelayedActionJoinListener(DelayedActionService delayedActionService) {
        this.delayedActionService = delayedActionService;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent playerJoinEvent) {
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> delayedActionService.processDelayedAction(playerJoinEvent.getPlayer()));
    }
}
