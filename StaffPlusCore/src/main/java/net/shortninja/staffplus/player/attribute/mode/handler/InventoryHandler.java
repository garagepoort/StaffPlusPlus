package net.shortninja.staffplus.player.attribute.mode.handler;

<<<<<<< HEAD
import org.bukkit.Bukkit;

=======
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InventoryHandler {

    private static List<UUID> virtualInvUsers;

<<<<<<< HEAD
    public InventoryHandler(){
        virtualInvUsers = new ArrayList<>();
    }

    public void addVirtualUser(UUID id){
        virtualInvUsers.add(id);
    }

    public boolean isInVirtualInv(UUID id){
        return virtualInvUsers.contains(id);
    }

    public void removeVirtualUser(UUID id){
=======
    public InventoryHandler() {
        virtualInvUsers = new ArrayList<>();
    }

    public void addVirtualUser(UUID id) {
        virtualInvUsers.add(id);
    }

    public boolean isInVirtualInv(UUID id) {
        return virtualInvUsers.contains(id);
    }

    public void removeVirtualUser(UUID id) {
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
        virtualInvUsers.remove(id);
    }
}
