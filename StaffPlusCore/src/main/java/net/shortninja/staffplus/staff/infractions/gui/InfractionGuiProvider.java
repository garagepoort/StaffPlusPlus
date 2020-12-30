package net.shortninja.staffplus.staff.infractions.gui;

import net.shortninja.staffplus.staff.infractions.Infraction;
import org.bukkit.inventory.ItemStack;

public interface InfractionGuiProvider<T extends Infraction> {

    String getType();

    ItemStack getMenuItem(T i);
}
