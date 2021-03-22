package net.shortninja.staffplus.core.domain.staff.staffchat.config;

public class StaffChatConfiguration {

    private final boolean enabled;
    private final boolean bungeeEnabled;
    private final String handle;
    private final String permissionStaffChat;

    public StaffChatConfiguration(boolean enabled,
                                  boolean bungeeEnabled,
                                  String handle, String permissionStaffChat) {
        this.enabled = enabled;
        this.bungeeEnabled = bungeeEnabled;
        this.handle = handle;
        this.permissionStaffChat = permissionStaffChat;
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
}
