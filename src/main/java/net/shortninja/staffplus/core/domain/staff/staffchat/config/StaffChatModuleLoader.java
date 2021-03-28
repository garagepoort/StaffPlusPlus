package net.shortninja.staffplus.core.domain.staff.staffchat.config;

import net.shortninja.staffplus.core.common.config.ConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;

public class StaffChatModuleLoader extends ConfigLoader<StaffChatConfiguration> {

    @Override
    protected StaffChatConfiguration load(FileConfiguration config) {
        boolean staffChatEnabled = config.getBoolean("staff-chat-module.enabled");
        boolean bungeeEnabled = config.getBoolean("staff-chat-module.bungee");
        String staffChatHandle = config.getString("staff-chat-module.handle");
        String commandStaffChatMute = config.getString("commands.staff-chat-mute");
        String permissionStaffChat = config.getString("permissions.staff-chat");
        String permissionStaffChatMute = config.getString("permissions.staff-chat-mute");
        return new StaffChatConfiguration(staffChatEnabled, bungeeEnabled, staffChatHandle, commandStaffChatMute, permissionStaffChat, permissionStaffChatMute);
    }
}
