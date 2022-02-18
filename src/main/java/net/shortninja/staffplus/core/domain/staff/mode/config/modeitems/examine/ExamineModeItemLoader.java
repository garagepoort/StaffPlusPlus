package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.examine;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemLoader;

@IocBean
public class ExamineModeItemLoader extends ModeItemLoader<ExamineModeConfiguration> {
    public ExamineModeItemLoader(IProtocolService protocolService) {
        super(protocolService);
    }

    @Override
    protected String getModuleName() {
        return "examine-module";
    }

    @Override
    protected ExamineModeConfiguration load() {
        ExamineModeConfiguration modeItemConfiguration = new ExamineModeConfiguration(getModuleName(),
            staffModeModulesConfig.getString("modules.examine-module.title"),
            getModeItemLocation("modules.examine-module.info-line.food"),
            getModeItemLocation("modules.examine-module.info-line.ip-address"),
            getModeItemLocation("modules.examine-module.info-line.gamemode"),
            getModeItemLocation("modules.examine-module.info-line.infractions"),
            getModeItemLocation("modules.examine-module.info-line.location"),
            getModeItemLocation("modules.examine-module.info-line.notes"),
            getModeItemLocation("modules.examine-module.info-line.freeze"),
            getModeItemLocation("modules.examine-module.info-line.warn"));
        return super.loadGeneralConfig(modeItemConfiguration);
    }

    private int getModeItemLocation(String s) {
        return staffModeModulesConfig.getInt(s) <= 0 ? -1 : staffModeModulesConfig.getInt(s);
    }
}
