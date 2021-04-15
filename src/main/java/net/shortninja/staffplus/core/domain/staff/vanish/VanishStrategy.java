package net.shortninja.staffplus.core.domain.staff.vanish;

import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.entity.Player;

public interface VanishStrategy {

    void vanish(Player player);

    void unvanish(Player player);

    /**
     * Triggered when a new player joins.
     * Make sure all currently vanished players are not visible for the given recently join player
     * @param player
     */
    void updateVanish(Player player);

    VanishType getVanishType();
}
