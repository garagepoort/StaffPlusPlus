package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.counter;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemLoader;
import org.bukkit.configuration.file.FileConfiguration;

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
    protected CounterModeConfiguration load(FileConfiguration config) {
        CounterModeConfiguration modeItemConfiguration = new CounterModeConfiguration(getModuleName(),
            config.getBoolean("staff-mode.counter-module.show-staff-mode"),
            config.getString("staff-mode.counter-module.title"));
        return super.loadGeneralConfig(config, modeItemConfiguration);
    }
}
