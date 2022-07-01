package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.freeze;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigurationLoader;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemLoader;

@IocBean
public class FreezeModeItemLoader extends ModeItemLoader<FreezeModeStaffModuleConfiguration> {
    public FreezeModeItemLoader(IProtocolService protocolService, ConfigurationLoader configurationLoader) {
        super(protocolService, configurationLoader);
    }

    @Override
    protected String getModuleName() {
        return "freeze-module";
    }

    @Override
    protected FreezeModeStaffModuleConfiguration load() {
        FreezeModeStaffModuleConfiguration modeItemConfiguration = new FreezeModeStaffModuleConfiguration(getModuleName());
        return super.loadGeneralConfig(modeItemConfiguration);
    }
}
