package net.shortninja.staffplus.domain.staff.mode.config.modeitems.randomteleport;

import net.shortninja.staffplus.domain.staff.mode.config.ModeItemConfiguration;

public class RandomTeleportModeConfiguration extends ModeItemConfiguration {

    private boolean random;

    public RandomTeleportModeConfiguration(String identifier, boolean random) {
        super(identifier);
        this.random = random;
    }

    public boolean isRandom() {
        return random;
    }
}
