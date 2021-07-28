package net.shortninja.staffplus.core.domain.staff.infractions.gui.views;

import net.shortninja.staffplus.core.domain.staff.infractions.Infraction;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionType;
import org.bukkit.inventory.ItemStack;

public interface InfractionGuiProvider<T extends Infraction> {

    InfractionType getType();

    ItemStack getMenuItem(T i);
}
