package net.shortninja.staffplus.core.domain.staff.staffchat;

import net.shortninja.staffplus.core.common.Sounds;

import java.util.Optional;

public class StaffChatChannelConfiguration {

    private final String name;
    private final String command;
    private final String permission;
    private final String handle;
    private final String prefix;
    private final String messageFormat;
    private final Sounds sounds;

    public StaffChatChannelConfiguration(String name, String command, String permission, String handle, String prefix, String messageFormat, Sounds sounds) {
        this.name = name;
        this.command = command;
        this.permission = permission;
        this.handle = handle;
        this.prefix = prefix;
        this.messageFormat = messageFormat;
        this.sounds = sounds;
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
    
    public Optional<Sounds> getNotificationSound() {
        return Optional.ofNullable(sounds);
    }

    public String getPrefix() {
        return prefix;
    }

    public String getMessageFormat() {
        return messageFormat;
    }
}
