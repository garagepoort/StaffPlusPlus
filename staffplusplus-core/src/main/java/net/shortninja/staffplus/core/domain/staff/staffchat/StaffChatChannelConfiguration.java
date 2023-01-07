package net.shortninja.staffplus.core.domain.staff.staffchat;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import net.shortninja.staffplus.core.application.config.SoundsConfigTransformer;
import net.shortninja.staffplus.core.common.Sounds;

import java.util.Optional;

public class StaffChatChannelConfiguration {

    @ConfigProperty("name")
    private String name;
    @ConfigProperty("command")
    private String command;
    @ConfigProperty("permission")
    private String permission;
    @ConfigProperty("handle")
    private String handle;
    @ConfigProperty("prefix")
    private String prefix;
    @ConfigProperty("message-format")
    private String messageFormat;
    @ConfigProperty("sound")
    @ConfigTransformer(SoundsConfigTransformer.class)
    private Sounds sounds;

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
