package net.shortninja.staffplus.staff.mute.config;

import net.shortninja.staffplus.common.config.ConfigLoader;
import net.shortninja.staffplus.common.config.GuiItemConfig;
import org.bukkit.configuration.file.FileConfiguration;

public class MuteModuleLoader extends ConfigLoader<MuteConfiguration> {

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
        String permissionMuteByPass = config.getString("permissions.mute-bypass");

        return new MuteConfiguration(muteEnabled, commandMutePlayer, commandTempMutePlayer, commandUnmutePlayer, permissionMutePlayer, permissionMuteByPass, guiItemConfig);
    }
}
