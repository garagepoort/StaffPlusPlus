package net.shortninja.staffplus.core.domain.chatchannels.bungee.dto;

import net.shortninja.staffplusplus.chatchannels.ChatChannelPlayerLeftEvent;

import java.util.UUID;

public class ChatChannelPlayerLeftBungeeDto extends ChatChannelBungeeDto {

    private final String playerName;
    private final UUID playerUuid;

    public ChatChannelPlayerLeftBungeeDto(ChatChannelPlayerLeftEvent event) {
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
