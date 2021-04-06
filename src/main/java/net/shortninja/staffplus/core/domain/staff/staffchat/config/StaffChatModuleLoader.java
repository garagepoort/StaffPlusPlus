package net.shortninja.staffplus.core.domain.staff.staffchat.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

@IocBean
public class StaffChatModuleLoader extends AbstractConfigLoader<StaffChatConfiguration> {

    @Override
    protected StaffChatConfiguration load() {
        boolean staffChatEnabled = defaultConfig.getBoolean("staff-chat-module.enabled");
        boolean bungeeEnabled = defaultConfig.getBoolean("staff-chat-module.bungee");
        String staffChatHandle = defaultConfig.getString("staff-chat-module.handle");
        String commandStaffChatMute = defaultConfig.getString("commands.staff-chat-mute");
        String permissionStaffChat = defaultConfig.getString("permissions.staff-chat");
        String permissionStaffChatMute = defaultConfig.getString("permissions.staff-chat-mute");
        return new StaffChatConfiguration(staffChatEnabled, bungeeEnabled, staffChatHandle, commandStaffChatMute, permissionStaffChat, permissionStaffChatMute);
    }
}
