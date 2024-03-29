package net.shortninja.staffplus.core.domain.chatchannels.bungee.dto;

import net.shortninja.staffplusplus.chatchannels.ChatChannelMessageSendEvent;

import java.util.UUID;

public class ChatChannelMessageBungeeDto extends ChatChannelBungeeDto {

    private final String message;
    private final String senderName;
    private final UUID senderUuid;

    public ChatChannelMessageBungeeDto(String message, ChatChannelMessageSendEvent event) {
        super(event.getChannel());
        this.message = message;
        this.senderName = event.getSender().getUsername();
        this.senderUuid = event.getSender().getId();
    }

    public String getMessage() {
        return message;
    }

    public String getSenderName() {
        return senderName;
    }

    public UUID getSenderUuid() {
        return senderUuid;
    }
}
