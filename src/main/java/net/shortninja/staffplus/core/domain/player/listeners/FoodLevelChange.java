package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

@IocBean
public class FoodLevelChange implements Listener {
    private final SessionManagerImpl sessionManager;

    public FoodLevelChange(SessionManagerImpl sessionManager) {
        this.sessionManager = sessionManager;
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlace(FoodLevelChangeEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player)) {
            return;
        }

        PlayerSession session = sessionManager.get(entity.getUniqueId());
        if (!session.isInStaffMode() || session.getModeConfiguration().get().isModeHungerLoss()) {
            return;
        }

        event.setCancelled(true);
    }
}