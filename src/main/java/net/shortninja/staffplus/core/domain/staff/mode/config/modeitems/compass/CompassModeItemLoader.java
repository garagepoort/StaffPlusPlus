package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.compass;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemLoader;

@IocBean
public class CompassModeItemLoader extends ModeItemLoader<CompassModeConfiguration> {
    public CompassModeItemLoader(IProtocolService protocolService) {
        super(protocolService);
    }

    @Override
    protected String getModuleName() {
        return "compass-module";
    }

    @Override
    protected CompassModeConfiguration load() {
        int velocity = staffModeModulesConfig.getInt("modules.compass-module.velocity");

        CompassModeConfiguration modeItemConfiguration = new CompassModeConfiguration(getModuleName(), velocity);
        return super.loadGeneralConfig(modeItemConfiguration);
    }
}
