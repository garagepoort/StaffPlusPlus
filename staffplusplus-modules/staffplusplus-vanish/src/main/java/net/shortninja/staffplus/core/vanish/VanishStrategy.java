package net.shortninja.staffplus.core.vanish;

import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.vanish.VanishType;

public interface VanishStrategy {

    void vanish(SppPlayer player);

    void unvanish(SppPlayer player);

    /**
     * Triggered when a new player joins.
     * Make sure all currently vanished players are not visible for the given recently join player
     * @param player
     */
    void updateVanish(SppPlayer player);

    VanishType getVanishType();
}
