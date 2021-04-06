package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.freeze;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.JavaUtils;

import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemLoader;

@IocBean
public class FreezeModeItemLoader extends ModeItemLoader<FreezeModeConfiguration> {
    public FreezeModeItemLoader(IProtocolService protocolService) {
        super(protocolService);
    }

    @Override
    protected String getModuleName() {
        return "freeze-module";
    }

    @Override
    protected FreezeModeConfiguration load() {
        FreezeModeConfiguration modeItemConfiguration = new FreezeModeConfiguration(getModuleName(),
            staffModeModulesConfig.getInt("modules.freeze-module.timer"),
            stringToSound(sanitize(staffModeModulesConfig.getString("modules.freeze-module.sound"))),
            staffModeModulesConfig.getBoolean("modules.freeze-module.prompt"),
            staffModeModulesConfig.getString("modules.freeze-module.prompt-title"),
            staffModeModulesConfig.getBoolean("modules.freeze-module.chat"),
            staffModeModulesConfig.getBoolean("modules.freeze-module.damage"),
            staffModeModulesConfig.getBoolean("modules.freeze-module.title-message-enabled"),
            JavaUtils.stringToList(staffModeModulesConfig.getString("modules.freeze-module.logout-commands")));
        return super.loadGeneralConfig(modeItemConfiguration);
    }
}
