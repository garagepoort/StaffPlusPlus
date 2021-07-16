package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.freeze;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemLoader;

import java.util.ArrayList;
import java.util.Arrays;

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
        String commas = staffModeModulesConfig.getString("modules.freeze-module.logout-commands");
        if (commas == null) {
            throw new IllegalArgumentException("Commas may not be null.");
        }

        FreezeModeConfiguration modeItemConfiguration = new FreezeModeConfiguration(getModuleName(),
            staffModeModulesConfig.getInt("modules.freeze-module.timer"),
            stringToSound(sanitize(staffModeModulesConfig.getString("modules.freeze-module.sound"))),
            staffModeModulesConfig.getBoolean("modules.freeze-module.prompt"),
            staffModeModulesConfig.getString("modules.freeze-module.prompt-title"),
            staffModeModulesConfig.getBoolean("modules.freeze-module.chat"),
            staffModeModulesConfig.getBoolean("modules.freeze-module.damage"),
            staffModeModulesConfig.getBoolean("modules.freeze-module.title-message-enabled"),
                new ArrayList<String>(Arrays.asList(commas.split("\\s*,\\s*"))));
        return super.loadGeneralConfig(modeItemConfiguration);
    }
}
