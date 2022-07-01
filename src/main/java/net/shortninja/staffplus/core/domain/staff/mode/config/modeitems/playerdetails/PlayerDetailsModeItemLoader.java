package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.playerdetails;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigurationLoader;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemLoader;

@IocBean
public class PlayerDetailsModeItemLoader extends ModeItemLoader<PlayerDetailsModeConfiguration> {
    public PlayerDetailsModeItemLoader(IProtocolService protocolService, ConfigurationLoader configurationLoader) {
        super(protocolService, configurationLoader);
    }

    @Override
    protected String getModuleName() {
        return "player-details-module";
    }

    @Override
    protected PlayerDetailsModeConfiguration load() {
        PlayerDetailsModeConfiguration modeItemConfiguration = new PlayerDetailsModeConfiguration(getModuleName());
        return super.loadGeneralConfig(modeItemConfiguration);
    }
}
