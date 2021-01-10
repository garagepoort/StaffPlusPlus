package net.shortninja.staffplus.staff.mode.config.modeitems.vanish;

import net.shortninja.staffplus.staff.mode.config.ModeItemConfiguration;
import org.bukkit.inventory.ItemStack;

public class VanishModeConfiguration extends ModeItemConfiguration {

    private ItemStack modeVanishItemOff;

    public VanishModeConfiguration(ItemStack modeVanishItemOff) {
        this.modeVanishItemOff = modeVanishItemOff;
    }

    public ItemStack getModeVanishItemOff() {
        return modeVanishItemOff;
    }
}
