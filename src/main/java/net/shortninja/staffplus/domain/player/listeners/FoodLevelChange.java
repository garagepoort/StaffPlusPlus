package net.shortninja.staffplus.domain.player.listeners;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChange implements Listener {
    private Options options = IocContainer.getOptions();
    private final SessionManagerImpl sessionManager = IocContainer.getSessionManager();

    public FoodLevelChange() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlace(FoodLevelChangeEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player)) {
            return;
        }

        PlayerSession session = sessionManager.get(entity.getUniqueId());
        if (options.modeConfiguration.isModeHungerLoss() || !session.isInStaffMode()) {
            return;
        }

        event.setCancelled(true);
    }
}