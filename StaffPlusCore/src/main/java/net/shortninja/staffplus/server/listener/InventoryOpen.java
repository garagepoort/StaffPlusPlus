package net.shortninja.staffplus.server.listener;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.listener.player.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class InventoryOpen implements Listener {

<<<<<<< HEAD
    public InventoryOpen(){
=======
    public InventoryOpen() {
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
        Bukkit.getServer().getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler
<<<<<<< HEAD
    public void onOpen(InventoryOpenEvent event){
        if(PlayerJoin.needLogin.contains(event.getPlayer().getUniqueId())){
=======
    public void onOpen(InventoryOpenEvent event) {
        if (PlayerJoin.needLogin.contains(event.getPlayer().getUniqueId())) {
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
            event.setCancelled(true);
        }
    }
}
