package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.follow;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemLoader;

@IocBean
public class FollowModeItemLoader extends ModeItemLoader<FollowModeConfiguration> {
    public FollowModeItemLoader(IProtocolService protocolService) {
        super(protocolService);
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
