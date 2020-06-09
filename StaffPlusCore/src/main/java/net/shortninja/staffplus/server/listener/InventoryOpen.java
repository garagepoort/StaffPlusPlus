package net.shortninja.staffplus.server.listener;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.listener.player.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class InventoryOpen implements Listener {

    public InventoryOpen(){
        Bukkit.getServer().getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if (PlayerJoin.needLogin.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
