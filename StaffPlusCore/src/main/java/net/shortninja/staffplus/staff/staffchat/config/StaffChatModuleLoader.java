package net.shortninja.staffplus.staff.staffchat.config;

import net.shortninja.staffplus.common.config.ConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;

public class StaffChatModuleLoader extends ConfigLoader<StaffChatConfiguration> {

    @Override
    protected StaffChatConfiguration load(FileConfiguration config) {
        boolean staffChatEnabled = config.getBoolean("staff-chat-module.enabled");
        boolean bungeeEnabled = config.getBoolean("staff-chat-module.bungee");
        String staffChatHandle = config.getString("staff-chat-module.handle");
        String permissionStaffChat = config.getString("permissions.staff-chat");
        return new StaffChatConfiguration(staffChatEnabled, bungeeEnabled, staffChatHandle, permissionStaffChat);
    }
}
