package net.shortninja.staffplus.core.mode.listeners.bukkit;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.mode.config.GeneralModeConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.util.Optional;

@IocBukkitListener
public class FoodLevelChange implements Listener {
    private final OnlineSessionsManager sessionManager;

    public FoodLevelChange(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlace(FoodLevelChangeEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player)) {
            return;
        }

        OnlinePlayerSession session = sessionManager.get((Player) entity);
        Optional<GeneralModeConfiguration> mode = session.get("modeConfig");
        if (session.isInStaffMode() && !mode.get().isModeHungerLoss()) {
            event.setCancelled(true);
        }
    }
}