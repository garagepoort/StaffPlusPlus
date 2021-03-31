package net.shortninja.staffplus.core.domain.staff.mute.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

import net.shortninja.staffplus.core.common.gui.GuiItemConfig;
import org.bukkit.configuration.file.FileConfiguration;

@IocBean
public class MuteModuleLoader extends AbstractConfigLoader<MuteConfiguration> {

    @Override
    protected MuteConfiguration load(FileConfiguration config) {
        boolean muteEnabled = config.getBoolean("mute-module.enabled");

        boolean modeGuiMute = config.getBoolean("staff-mode.gui-module.mute-gui");
        String modeGuiMuteTitle = config.getString("staff-mode.gui-module.mute-title");
        String modeGuiMuteName = config.getString("staff-mode.gui-module.mute-name");
        String modeGuiMuteLore = config.getString("staff-mode.gui-module.mute-lore");
        GuiItemConfig guiItemConfig = new GuiItemConfig(modeGuiMute, modeGuiMuteTitle, modeGuiMuteName, modeGuiMuteLore);

        String commandMutePlayer = config.getString("commands.mute");
        String commandUnmutePlayer = config.getString("commands.unmute");
        String commandTempMutePlayer = config.getString("commands.tempmute");

        String permissionMutePlayer = config.getString("permissions.mute");
        String permissionUnmutePlayer = config.getString("permissions.unmute");
        String permissionMuteByPass = config.getString("permissions.mute-bypass");

        return new MuteConfiguration(muteEnabled, commandMutePlayer, commandTempMutePlayer, commandUnmutePlayer, permissionMutePlayer, permissionUnmutePlayer, permissionMuteByPass, guiItemConfig);
    }
}
