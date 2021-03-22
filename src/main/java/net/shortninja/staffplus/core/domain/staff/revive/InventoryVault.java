package net.shortninja.staffplus.core.domain.staff.revive;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class InventoryVault {
    private UUID uuid;
    private ItemStack[] items;
    private ItemStack[] armor;
    private ItemStack[] offHand;

    public InventoryVault(UUID uuid, ItemStack[] items, ItemStack[] armor, ItemStack[] offHand) {
        this.items = items;
        this.armor = armor;
        this.offHand = offHand;
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public ItemStack[] getInventory(){
        return items;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public ItemStack[] getOffHand() {
        return offHand;
    }

}