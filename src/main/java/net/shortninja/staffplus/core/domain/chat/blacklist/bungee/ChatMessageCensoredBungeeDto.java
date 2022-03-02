package net.shortninja.staffplus.core.domain.chat.blacklist.bungee;

import net.shortninja.staffplus.core.common.bungee.BungeeMessage;
import net.shortninja.staffplusplus.chat.ChatMessageCensoredEvent;

import java.util.UUID;

public class ChatMessageCensoredBungeeDto extends BungeeMessage {

    private final UUID playerUuid;
    private final String playerName;
    private final String censoredMessage;
    private final String originalMessage;

    public ChatMessageCensoredBungeeDto(ChatMessageCensoredEvent chatMessageCensoredEvent) {
        super(chatMessageCensoredEvent.getServerName());
        this.playerName = chatMessageCensoredEvent.getPlayer().getName();
        this.playerUuid = chatMessageCensoredEvent.getPlayer().getUniqueId();
        this.censoredMessage = chatMessageCensoredEvent.getCensoredMessage();
        this.originalMessage = chatMessageCensoredEvent.getOriginalMessage();
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getCensoredMessage() {
        return censoredMessage;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }
}
