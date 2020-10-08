package net.shortninja.staffplus.staff.ban.config;

import net.shortninja.staffplus.common.config.ConfigLoader;
import net.shortninja.staffplus.common.config.GuiItemConfig;

public class BanModuleLoader extends ConfigLoader<BanConfiguration> {

    @Override
    public BanConfiguration load() {
        boolean banEnabled = config.getBoolean("ban-module.enabled");

        boolean modeGuiBan = config.getBoolean("staff-mode.gui-module.ban-gui");
        String modeGuiBanTitle = config.getString("staff-mode.gui-module.ban-title");
        String modeGuiBanName = config.getString("staff-mode.gui-module.ban-name");
        String modeGuiBanLore = config.getString("staff-mode.gui-module.ban-lore");
        GuiItemConfig guiItemConfig = new GuiItemConfig(modeGuiBan, modeGuiBanTitle, modeGuiBanName, modeGuiBanLore);

        String commandBanPlayer = config.getString("commands.ban");
        String commandUnbanPlayer = config.getString("commands.unban");
        String commandTempBanPlayer = config.getString("commands.tempban");

        String permissionBanPlayer = config.getString("permissions.ban");
        String permissionBanByPass = config.getString("permissions.ban-bypass");

        return new BanConfiguration(banEnabled, commandBanPlayer, commandTempBanPlayer, commandUnbanPlayer, permissionBanPlayer, permissionBanByPass, guiItemConfig);
    }
}
