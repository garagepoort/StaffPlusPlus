package net.shortninja.staffplus.core.vanish.listeners;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@IocBukkitListener
public class VanishNormalChestOpening implements Listener {

    @ConfigProperty("vanish-module.normal-chest-opening")
    private boolean normalChestOpening;
    @ConfigProperty("vanish-module.normal-chest-interaction")
    private boolean normalChestInteraction;

    private final OnlineSessionsManager sessionManager;

    public VanishNormalChestOpening(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        OnlinePlayerSession playerSession = sessionManager.get(player);

        if (playerSession.isVanished() && checkingChest(event) && !normalChestOpening) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        OnlinePlayerSession playerSession = sessionManager.get(player);
        if(event.getClickedInventory() != null && event.getClickedInventory().getHolder() != null && event.getClickedInventory().getHolder() instanceof Container) {
            if (playerSession.isVanished() && !normalChestInteraction) {
                event.setCancelled(true);
            }
        }
    }

    private boolean checkingChest(PlayerInteractEvent event) {
        return event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof Container;
    }
}
