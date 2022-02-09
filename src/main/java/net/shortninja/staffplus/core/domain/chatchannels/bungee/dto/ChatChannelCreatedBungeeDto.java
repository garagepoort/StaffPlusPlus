package net.shortninja.staffplus.core.domain.chatchannels.bungee.dto;

import net.shortninja.staffplusplus.chatchannels.ChatChannelCreatedEvent;

public class ChatChannelCreatedBungeeDto extends ChatChannelBungeeDto {

    private final String openingMessage;

    public ChatChannelCreatedBungeeDto(String openingMessage, ChatChannelCreatedEvent event) {
        super(event.getChannel());
        this.openingMessage = openingMessage;
    }

    public String getOpeningMessage() {
        return openingMessage;
    }
}
