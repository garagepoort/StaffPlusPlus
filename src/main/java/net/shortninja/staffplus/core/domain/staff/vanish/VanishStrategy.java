package net.shortninja.staffplus.core.domain.staff.vanish;

import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.entity.Player;

public interface VanishStrategy {

    void vanish(Player player);

    void unvanish(Player player);

    VanishType getVanishType();
}
