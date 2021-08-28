package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.playerdetails;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemLoader;

@IocBean
public class PlayerDetailsModeItemLoader extends ModeItemLoader<PlayerDetailsModeConfiguration> {
    public PlayerDetailsModeItemLoader(IProtocolService protocolService) {
        super(protocolService);
    }

    @Override
    protected String getModuleName() {
        return "players-module";
    }

    @Override
    protected PlayerDetailsModeConfiguration load() {
        PlayerDetailsModeConfiguration modeItemConfiguration = new PlayerDetailsModeConfiguration(getModuleName());
        return super.loadGeneralConfig(modeItemConfiguration);
    }
}
