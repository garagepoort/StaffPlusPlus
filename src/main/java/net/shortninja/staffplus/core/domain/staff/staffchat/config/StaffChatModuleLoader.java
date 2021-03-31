package net.shortninja.staffplus.core.domain.staff.staffchat.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

import org.bukkit.configuration.file.FileConfiguration;

@IocBean
public class StaffChatModuleLoader extends AbstractConfigLoader<StaffChatConfiguration> {

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
