package net.shortninja.staffplus.staff.mode.config.modeitems.compass;

import net.shortninja.staffplus.staff.mode.config.ModeItemConfiguration;

public class CompassModeConfiguration extends ModeItemConfiguration {

    private int velocity;

    public CompassModeConfiguration(String identifier, int velocity) {
        super(identifier);
        this.velocity = velocity;
    }

    public int getVelocity() {
        return velocity;
    }
}
