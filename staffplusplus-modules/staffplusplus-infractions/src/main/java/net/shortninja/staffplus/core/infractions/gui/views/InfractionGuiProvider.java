package net.shortninja.staffplus.core.infractions.gui.views;

import net.shortninja.staffplus.core.infractions.Infraction;
import net.shortninja.staffplus.core.infractions.InfractionType;
import org.bukkit.inventory.ItemStack;

public interface InfractionGuiProvider<T extends Infraction> {

    InfractionType getType();

    ItemStack getMenuItem(T i);
}
