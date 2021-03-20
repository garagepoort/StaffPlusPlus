package net.shortninja.staffplus.domain.staff.infractions.gui;

import net.shortninja.staffplus.domain.staff.infractions.Infraction;
import net.shortninja.staffplus.domain.staff.infractions.InfractionType;
import org.bukkit.inventory.ItemStack;

public interface InfractionGuiProvider<T extends Infraction> {

    InfractionType getType();

    ItemStack getMenuItem(T i);
}
