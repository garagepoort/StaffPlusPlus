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
            staffModeModulesConfig.getInt("modules.examine-module.info-line.food") <= 0 ? -1 : staffModeModulesConfig.getInt("modules.examine-module.info-line.food"),
            staffModeModulesConfig.getInt("modules.examine-module.info-line.ip-address") <= 0 ? -1 : staffModeModulesConfig.getInt("modules.examine-module.info-line.ip-address"),
            staffModeModulesConfig.getInt("modules.examine-module.info-line.gamemode") <= 0 ? -1 : staffModeModulesConfig.getInt("modules.examine-module.info-line.gamemode"),
            staffModeModulesConfig.getInt("modules.examine-module.info-line.infractions") <= 0 ? -1 : staffModeModulesConfig.getInt("modules.examine-module.info-line.infractions"),
            staffModeModulesConfig.getInt("modules.examine-module.info-line.location") <= 0 ? -1 : staffModeModulesConfig.getInt("modules.examine-module.info-line.location"),
            staffModeModulesConfig.getInt("modules.examine-module.info-line.notes") <= 0 ? -1 : staffModeModulesConfig.getInt("modules.examine-module.info-line.notes"),
            staffModeModulesConfig.getInt("modules.examine-module.info-line.freeze") <= 0 ? -1 : staffModeModulesConfig.getInt("modules.examine-module.info-line.freeze"),
            staffModeModulesConfig.getInt("modules.examine-module.info-line.warn") <= 0 ? -1 : staffModeModulesConfig.getInt("modules.examine-module.info-line.warn"));
        return super.loadGeneralConfig(modeItemConfiguration);
    }
}
