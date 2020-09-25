package net.shortninja.staffplus.staff.mode.handler;



import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InventoryHandler {

    private static List<UUID> virtualInvUsers;


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
        virtualInvUsers.remove(id);
    }

}
