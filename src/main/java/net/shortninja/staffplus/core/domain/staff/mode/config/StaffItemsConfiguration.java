package net.shortninja.staffplus.core.domain.staff.mode.config;

import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.compass.CompassModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.counter.CounterModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.cps.CpsModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.follow.FollowModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.freeze.FreezeModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.gui.GuiModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.playerdetails.PlayerDetailsModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.randomteleport.RandomTeleportModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.vanish.VanishModeConfiguration;

public class StaffItemsConfiguration {

    private final CompassModeConfiguration compassModeConfiguration;
    private final CounterModeConfiguration counterModeConfiguration;
    private final CpsModeConfiguration cpsModeConfiguration;
    private final ExamineModeConfiguration examineModeConfiguration;
    private final FollowModeConfiguration followModeConfiguration;
    private final FreezeModeConfiguration freezeModeConfiguration;
    private final GuiModeConfiguration guiModeConfiguration;
    private final RandomTeleportModeConfiguration randomTeleportModeConfiguration;
    private final VanishModeConfiguration vanishModeConfiguration;
    private final PlayerDetailsModeConfiguration playerDetailsModeConfiguration;

    public StaffItemsConfiguration(CompassModeConfiguration compassModeConfiguration,
                                   CounterModeConfiguration counterModeConfiguration,
                                   CpsModeConfiguration cpsModeConfiguration,
                                   ExamineModeConfiguration examineModeConfiguration,
                                   FollowModeConfiguration followModeConfiguration,
                                   FreezeModeConfiguration freezeModeConfiguration,
                                   GuiModeConfiguration guiModeConfiguration,
                                   RandomTeleportModeConfiguration randomTeleportModeConfiguration,
                                   VanishModeConfiguration vanishModeConfiguration, PlayerDetailsModeConfiguration playerDetailsModeConfiguration) {
        this.compassModeConfiguration = compassModeConfiguration;
        this.counterModeConfiguration = counterModeConfiguration;
        this.cpsModeConfiguration = cpsModeConfiguration;
        this.examineModeConfiguration = examineModeConfiguration;
        this.followModeConfiguration = followModeConfiguration;
        this.freezeModeConfiguration = freezeModeConfiguration;
        this.guiModeConfiguration = guiModeConfiguration;
        this.randomTeleportModeConfiguration = randomTeleportModeConfiguration;
        this.vanishModeConfiguration = vanishModeConfiguration;

        this.playerDetailsModeConfiguration = playerDetailsModeConfiguration;
    }

    public CompassModeConfiguration getCompassModeConfiguration() {
        return compassModeConfiguration;
    }

    public VanishModeConfiguration getVanishModeConfiguration() {
        return vanishModeConfiguration;
    }

    public RandomTeleportModeConfiguration getRandomTeleportModeConfiguration() {
        return randomTeleportModeConfiguration;
    }

    public CounterModeConfiguration getCounterModeConfiguration() {
        return counterModeConfiguration;
    }

    public CpsModeConfiguration getCpsModeConfiguration() {
        return cpsModeConfiguration;
    }

    public ExamineModeConfiguration getExamineModeConfiguration() {
        return examineModeConfiguration;
    }

    public FollowModeConfiguration getFollowModeConfiguration() {
        return followModeConfiguration;
    }

    public FreezeModeConfiguration getFreezeModeConfiguration() {
        return freezeModeConfiguration;
    }

    public GuiModeConfiguration getGuiModeConfiguration() {
        return guiModeConfiguration;
    }

    public PlayerDetailsModeConfiguration getPlayerDetailsModeConfiguration() {
        return playerDetailsModeConfiguration;
    }
}
