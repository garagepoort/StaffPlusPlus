package net.shortninja.staffplus.core.domain.staff.staffchat;

import java.util.Optional;

public class StaffChatChannelConfiguration {

    private final String name;
    private String command;
    private String permission;
    private String handle;
    private String prefix;
    private final String messageFormat;

    public StaffChatChannelConfiguration(String name, String command, String permission, String handle, String prefix, String messageFormat) {
        this.name = name;
        this.command = command;
        this.permission = permission;
        this.handle = handle;
        this.prefix = prefix;
        this.messageFormat = messageFormat;
    }

    public String getName() {
        return name;
    }

    public String getCommand() {
        return command;
    }

    public Optional<String> getPermission() {
        return Optional.ofNullable(permission);
    }

    public Optional<String> getHandle() {
        return Optional.ofNullable(handle);
    }

    public String getPrefix() {
        return prefix;
    }

    public String getMessageFormat() {
        return messageFormat;
    }
}
