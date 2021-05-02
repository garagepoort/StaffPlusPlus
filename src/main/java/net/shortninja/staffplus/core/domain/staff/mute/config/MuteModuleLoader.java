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

        String commandMutePlayer = commandsConfig.getString("commands.mute");
        String commandUnmutePlayer = commandsConfig.getString("commands.unmute");
        String commandTempMutePlayer = commandsConfig.getString("commands.tempmute");

        String permissionMutePlayer = permissionsConfig.getString("permissions.mute");
        String permissionUnmutePlayer = permissionsConfig.getString("permissions.unmute");
        String permissionMuteByPass = permissionsConfig.getString("permissions.mute-bypass");
        String permissionMuteNotifications = permissionsConfig.getString("permissions.mute-notifications");

        return new MuteConfiguration(muteEnabled, commandMutePlayer, commandTempMutePlayer, commandUnmutePlayer, permissionMutePlayer, permissionUnmutePlayer, permissionMuteByPass, guiItemConfig, permissionMuteNotifications);
    }
}
