package net.shortninja.staffplus.core.common.nbt;

import be.garagepoort.mcioc.IocBean;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

@IocBean
public class NbtService {

    private static final String NBT_IDENTIFIER = "StaffPlus";

    public ItemStack addNbtString(String value, ItemStack modeVanishItem) {
        NBTItem nbtItem = new NBTItem(modeVanishItem);
        nbtItem.setString(NBT_IDENTIFIER, value);
        return nbtItem.getItem();
    }

    public String getNbtString(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getString(NBT_IDENTIFIER);
    }
}
