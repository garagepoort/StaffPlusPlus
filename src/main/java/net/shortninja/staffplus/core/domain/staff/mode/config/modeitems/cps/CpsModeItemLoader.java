package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.cps;

import be.garagepoort.mcioc.IocBean;

import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemLoader;
import org.bukkit.configuration.file.FileConfiguration;

@IocBean
public class CpsModeItemLoader extends ModeItemLoader<CpsModeConfiguration> {
    public CpsModeItemLoader(IProtocolService protocolService) {
        super(protocolService);
    }

    @Override
    protected String getModuleName() {
        return "cps-module";
    }

    @Override
    protected CpsModeConfiguration load(FileConfiguration config) {
        CpsModeConfiguration modeItemConfiguration = new CpsModeConfiguration(getModuleName(),
            config.getInt("staff-mode.cps-module.time") * 20,
            config.getInt("staff-mode.cps-module.max")
        );
        return super.loadGeneralConfig(config, modeItemConfiguration);
    }
}
