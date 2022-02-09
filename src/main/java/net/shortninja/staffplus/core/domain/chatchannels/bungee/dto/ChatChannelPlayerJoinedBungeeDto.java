package net.shortninja.staffplus.core.domain.chatchannels.bungee.dto;

import net.shortninja.staffplusplus.chatchannels.ChatChannelPlayerJoinedEvent;

import java.util.UUID;

public class ChatChannelPlayerJoinedBungeeDto extends ChatChannelBungeeDto {

    private final String playerName;
    private final UUID playerUuid;

    public ChatChannelPlayerJoinedBungeeDto(ChatChannelPlayerJoinedEvent event) {
        super(event.getChannel());
        this.playerName = event.getPlayer().getUsername();
        this.playerUuid = event.getPlayer().getId();
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }
}
