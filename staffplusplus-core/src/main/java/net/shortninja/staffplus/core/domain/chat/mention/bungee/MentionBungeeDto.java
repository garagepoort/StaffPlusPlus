package net.shortninja.staffplus.core.domain.chat.mention.bungee;

import net.shortninja.staffplus.core.common.bungee.BungeeMessage;
import net.shortninja.staffplusplus.chat.PlayerMentionedEvent;

import java.util.UUID;

public class MentionBungeeDto extends BungeeMessage {

    private final String playerName;
    private final UUID playerUuid;
    private final String mentionedPlayerName;
    private final UUID mentionedPlayerUuid;
    private final String chatMessage;

    public MentionBungeeDto(PlayerMentionedEvent playerMentionedEvent) {
        super(playerMentionedEvent.getServerName());
        this.playerName = playerMentionedEvent.getPlayer().getName();
        this.playerUuid = playerMentionedEvent.getPlayer().getUniqueId();
        this.mentionedPlayerName = playerMentionedEvent.getMentionedPlayer().getName();
        this.mentionedPlayerUuid = playerMentionedEvent.getMentionedPlayer().getUniqueId();
        this.chatMessage = playerMentionedEvent.getChatMessage();
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getMentionedPlayerName() {
        return mentionedPlayerName;
    }

    public UUID getMentionedPlayerUuid() {
        return mentionedPlayerUuid;
    }

    public String getChatMessage() {
        return chatMessage;
    }
}
