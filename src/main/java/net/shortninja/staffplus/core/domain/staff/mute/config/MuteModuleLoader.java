package net.shortninja.staffplus.core.domain.staff.mute.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

import net.shortninja.staffplus.core.common.gui.GuiItemConfig;

@IocBean
public class MuteModuleLoader extends AbstractConfigLoader<MuteConfiguration> {

    @Override
    protected MuteConfiguration load() {
        boolean muteEnabled = defaultConfig.getBoolean("mute-module.enabled");

        boolean modeGuiMute = staffModeModulesConfig.getBoolean("modules.gui-module.mute-gui");
        String modeGuiMuteTitle = staffModeModulesConfig.getString("modules.gui-module.mute-title");
        String modeGuiMuteName = staffModeModulesConfig.getString("modules.gui-module.mute-name");
        String modeGuiMuteLore = staffModeModulesConfig.getString("modules.gui-module.mute-lore");
        GuiItemConfig guiItemConfig = new GuiItemConfig(modeGuiMute, modeGuiMuteTitle, modeGuiMuteName, modeGuiMuteLore);

        String commandMutePlayer = defaultConfig.getString("commands.mute");
        String commandUnmutePlayer = defaultConfig.getString("commands.unmute");
        String commandTempMutePlayer = defaultConfig.getString("commands.tempmute");

        String permissionMutePlayer = defaultConfig.getString("permissions.mute");
        String permissionUnmutePlayer = defaultConfig.getString("permissions.unmute");
        String permissionMuteByPass = defaultConfig.getString("permissions.mute-bypass");

        return new MuteConfiguration(muteEnabled, commandMutePlayer, commandTempMutePlayer, commandUnmutePlayer, permissionMutePlayer, permissionUnmutePlayer, permissionMuteByPass, guiItemConfig);
    }
}
