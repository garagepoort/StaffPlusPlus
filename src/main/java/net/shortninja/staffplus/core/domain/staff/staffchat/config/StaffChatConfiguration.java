package net.shortninja.staffplus.core.domain.staff.staffchat.config;

public class StaffChatConfiguration {

    private final boolean enabled;
    private final boolean bungeeEnabled;
    private final String handle;
    private final String commandStaffChatMute;
    private final String permissionStaffChat;
    private final String permissionStaffChatMute;

    public StaffChatConfiguration(boolean enabled,
                                  boolean bungeeEnabled,
                                  String handle,
                                  String commandStaffChatMute,
                                  String permissionStaffChat, String permissionStaffChatMute) {
        this.enabled = enabled;
        this.bungeeEnabled = bungeeEnabled;
        this.handle = handle;
        this.commandStaffChatMute = commandStaffChatMute;
        this.permissionStaffChat = permissionStaffChat;
        this.permissionStaffChatMute = permissionStaffChatMute;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isBungeeEnabled() {
        return bungeeEnabled;
    }

    public String getHandle() {
        return handle;
    }

    public String getPermissionStaffChat() {
        return permissionStaffChat;
    }

    public String getPermissionStaffChatMute() {
        return permissionStaffChatMute;
    }

    public String getCommandStaffChatMute() {
        return commandStaffChatMute;
    }
}
