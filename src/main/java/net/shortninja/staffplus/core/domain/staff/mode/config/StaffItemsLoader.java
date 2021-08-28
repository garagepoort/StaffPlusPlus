package net.shortninja.staffplus.core.domain.staff.mode.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.AbstractConfigLoader;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.compass.CompassModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.compass.CompassModeItemLoader;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.counter.CounterModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.counter.CounterModeItemLoader;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.cps.CpsModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.cps.CpsModeItemLoader;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.examine.ExamineModeItemLoader;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.follow.FollowModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.follow.FollowModeItemLoader;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.freeze.FreezeModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.freeze.FreezeModeItemLoader;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.gui.GuiModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.gui.GuiModeItemLoader;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.playerdetails.PlayerDetailsModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.playerdetails.PlayerDetailsModeItemLoader;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.randomteleport.RandomTeleportModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.randomteleport.RandomTeleportModeItemLoader;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.vanish.VanishModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.vanish.VanishModeItemLoader;

@IocBean
public class StaffItemsLoader extends AbstractConfigLoader<StaffItemsConfiguration> {

    private final CompassModeItemLoader compassModeItemLoader;
    private final CounterModeItemLoader counterModeItemLoader;
    private final CpsModeItemLoader cpsModeItemLoader;
    private final ExamineModeItemLoader examineModeItemLoader;
    private final FollowModeItemLoader followModeItemLoader;
    private final FreezeModeItemLoader freezeModeItemLoader;
    private final GuiModeItemLoader guiModeItemLoader;
    private final RandomTeleportModeItemLoader randomTeleportModeItemLoader;
    private final VanishModeItemLoader vanishModeItemLoader;
    private final PlayerDetailsModeItemLoader playerDetailsModeItemLoader;

    public StaffItemsLoader(CompassModeItemLoader compassModeItemLoader,
                            CounterModeItemLoader counterModeItemLoader,
                            CpsModeItemLoader cpsModeItemLoader,
                            ExamineModeItemLoader examineModeItemLoader,
                            FollowModeItemLoader followModeItemLoader,
                            FreezeModeItemLoader freezeModeItemLoader,
                            GuiModeItemLoader guiModeItemLoader,
                            RandomTeleportModeItemLoader randomTeleportModeItemLoader,
                            VanishModeItemLoader vanishModeItemLoader,
                            PlayerDetailsModeItemLoader playerDetailsModeItemLoader) {
        this.compassModeItemLoader = compassModeItemLoader;
        this.counterModeItemLoader = counterModeItemLoader;
        this.cpsModeItemLoader = cpsModeItemLoader;
        this.examineModeItemLoader = examineModeItemLoader;
        this.followModeItemLoader = followModeItemLoader;
        this.freezeModeItemLoader = freezeModeItemLoader;
        this.guiModeItemLoader = guiModeItemLoader;
        this.randomTeleportModeItemLoader = randomTeleportModeItemLoader;
        this.vanishModeItemLoader = vanishModeItemLoader;
        this.playerDetailsModeItemLoader = playerDetailsModeItemLoader;
    }

    @Override
    protected StaffItemsConfiguration load() {
        CompassModeConfiguration compassModeConfiguration = compassModeItemLoader.loadConfig();
        CounterModeConfiguration counterModeConfiguration = counterModeItemLoader.loadConfig();
        CpsModeConfiguration cpsModeConfiguration = cpsModeItemLoader.loadConfig();
        ExamineModeConfiguration examineModeConfiguration = examineModeItemLoader.loadConfig();
        FollowModeConfiguration followModeConfiguration = followModeItemLoader.loadConfig();
        FreezeModeConfiguration freezeModeConfiguration = freezeModeItemLoader.loadConfig();
        GuiModeConfiguration guiModeConfiguration = guiModeItemLoader.loadConfig();
        RandomTeleportModeConfiguration randomTeleportModeConfiguration = randomTeleportModeItemLoader.loadConfig();
        VanishModeConfiguration vanishModeConfiguration = vanishModeItemLoader.loadConfig();
        PlayerDetailsModeConfiguration playerDetailsModeConfiguration = playerDetailsModeItemLoader.loadConfig();

        return new StaffItemsConfiguration(
            compassModeConfiguration,
            counterModeConfiguration,
            cpsModeConfiguration,
            examineModeConfiguration,
            followModeConfiguration,
            freezeModeConfiguration,
            guiModeConfiguration,
            randomTeleportModeConfiguration,
            vanishModeConfiguration,
            playerDetailsModeConfiguration);
    }
}
