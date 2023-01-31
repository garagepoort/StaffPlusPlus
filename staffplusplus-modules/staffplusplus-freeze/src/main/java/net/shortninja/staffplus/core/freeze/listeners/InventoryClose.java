package net.shortninja.staffplus.core.freeze.listeners;

import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.freeze.FreezeGui;
import net.shortninja.staffplus.core.freeze.config.FreezeConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

@IocBukkitListener
public class InventoryClose implements Listener {
    private final OnlineSessionsManager sessionManager;
    private final FreezeConfiguration freezeConfiguration;

    public InventoryClose(OnlineSessionsManager sessionManager,
                          FreezeConfiguration freezeConfiguration) {
        this.sessionManager = sessionManager;
        this.freezeConfiguration = freezeConfiguration;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClose(InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();
        // Do not run if session does not exists.
        // Reason is that it can result in the session being initialized on the main thread.
        if (sessionManager.has(player.getUniqueId())) {
            OnlinePlayerSession playerSession = sessionManager.get(player);
            if (playerSession.isFrozen() && freezeConfiguration.prompt) {
                Bukkit.getScheduler().runTaskLater(TubingBukkitPlugin.getPlugin(), () -> new FreezeGui(freezeConfiguration.promptTitle).show(player), 1);
            }
        }

    }
}