package net.shortninja.staffplus.staff.ban.config;

import net.shortninja.staffplus.common.config.ConfigLoader;
import net.shortninja.staffplus.common.config.GuiItemConfig;
import org.bukkit.configuration.file.FileConfiguration;

public class BanModuleLoader extends ConfigLoader<BanConfiguration> {

    @Override
    protected BanConfiguration load(FileConfiguration config) {
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
        String permissionUnbanPlayer = config.getString("permissions.unban");
        String permissionBanByPass = config.getString("permissions.ban-bypass");

        return new BanConfiguration(banEnabled, commandBanPlayer, commandTempBanPlayer, commandUnbanPlayer, permissionBanPlayer, permissionUnbanPlayer, permissionBanByPass, guiItemConfig);
    }
}
