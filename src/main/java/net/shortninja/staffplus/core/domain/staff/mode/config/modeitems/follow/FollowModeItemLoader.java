package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.follow;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigurationLoader;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemLoader;

@IocBean
public class FollowModeItemLoader extends ModeItemLoader<FollowModeConfiguration> {
    public FollowModeItemLoader(IProtocolService protocolService, ConfigurationLoader configurationLoader) {
        super(protocolService, configurationLoader);
    }

    @Override
    protected String getModuleName() {
        return "follow-module";
    }

    @Override
    protected FollowModeConfiguration load() {
        FollowModeConfiguration modeItemConfiguration = new FollowModeConfiguration(getModuleName());
        return super.loadGeneralConfig(modeItemConfiguration);
    }
}
