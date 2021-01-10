package net.shortninja.staffplus.staff.mode.config.modeitems.randomteleport;

import net.shortninja.staffplus.staff.mode.config.ModeItemConfiguration;

public class RandomTeleportModeConfiguration extends ModeItemConfiguration {

    private boolean random;

    public RandomTeleportModeConfiguration(boolean random) {
        this.random = random;
    }

    public boolean isRandom() {
        return random;
    }
}
