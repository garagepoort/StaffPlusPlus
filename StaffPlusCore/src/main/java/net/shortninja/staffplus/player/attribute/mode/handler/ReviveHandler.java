package net.shortninja.staffplus.player.attribute.mode.handler;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.player.attribute.mode.ModeDataVault;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReviveHandler {
    private static Map<UUID, ModeDataVault> savedInventories = new HashMap<UUID, ModeDataVault>();
    private MessageCoordinator message = StaffPlus.get().message;
    private Messages messages = StaffPlus.get().messages;

    public boolean hasSavedInventory(UUID uuid) {
        return savedInventories.containsKey(uuid);
    }

    public void cacheInventory(Player player) {
        UUID uuid = player.getUniqueId();
        ModeDataVault modeDataVault;
        if(!StaffPlus.get().nineteenPlus)
            modeDataVault = new ModeDataVault(uuid, ModeCoordinator.getContents(player), player.getInventory().getArmorContents());
        else
            modeDataVault = new ModeDataVault(uuid,ModeCoordinator.getContents(player),player.getInventory().getArmorContents(),player.getInventory().getExtraContents());
        savedInventories.put(uuid, modeDataVault);
    }

    public void restoreInventory(Player player) {
        UUID uuid = player.getUniqueId();
        ModeDataVault modeDataVault = savedInventories.get(uuid);

        JavaUtils.clearInventory(player);
        getItems(player,modeDataVault);
        player.getInventory().setArmorContents(modeDataVault.getArmor());
        if(StaffPlus.get().nineteenPlus)
            player.getInventory().setExtraContents(modeDataVault.getOffHand());
        message.send(player, messages.revivedUser, messages.prefixGeneral);
        savedInventories.remove(uuid);
    }

    private void getItems(Player p, ModeDataVault modeDataVault){
        HashMap<String, ItemStack> items = modeDataVault.getItems();
        for(String num : items.keySet())
            p.getInventory().setItem(Integer.parseInt(num),items.get(num));
    }
}