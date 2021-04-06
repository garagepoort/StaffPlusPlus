package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.counter;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemLoader;

@IocBean
public class CounterModeItemLoader extends ModeItemLoader<CounterModeConfiguration> {
    public CounterModeItemLoader(IProtocolService protocolService) {
        super(protocolService);
    }

    @Override
    protected String getModuleName() {
        return "counter-module";
    }

    @Override
    protected CounterModeConfiguration load() {
        CounterModeConfiguration modeItemConfiguration = new CounterModeConfiguration(getModuleName(),
            staffModeModulesConfig.getBoolean("modules.counter-module.show-staff-mode"),
            staffModeModulesConfig.getString("modules.counter-module.title"));
        return super.loadGeneralConfig(modeItemConfiguration);
    }
}
