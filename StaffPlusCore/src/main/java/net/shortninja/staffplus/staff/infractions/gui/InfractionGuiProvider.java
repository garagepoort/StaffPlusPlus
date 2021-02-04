package net.shortninja.staffplus.staff.infractions.gui;

import net.shortninja.staffplus.staff.infractions.Infraction;
import net.shortninja.staffplus.staff.infractions.InfractionType;
import org.bukkit.inventory.ItemStack;

public interface InfractionGuiProvider<T extends Infraction> {

    InfractionType getType();

    ItemStack getMenuItem(T i);
}
