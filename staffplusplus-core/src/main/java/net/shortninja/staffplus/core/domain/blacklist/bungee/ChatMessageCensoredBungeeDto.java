package net.shortninja.staffplus.core.domain.blacklist.bungee;

import net.shortninja.staffplus.core.common.bungee.BungeeMessage;
import net.shortninja.staffplusplus.blacklist.BlacklistCensoredEvent;
import net.shortninja.staffplusplus.blacklist.BlacklistType;

import java.util.UUID;

public class ChatMessageCensoredBungeeDto extends BungeeMessage {

    private final UUID playerUuid;
    private final String playerName;
    private final String censoredMessage;
    private final String originalMessage;
    private final BlacklistType blacklistType;

    public ChatMessageCensoredBungeeDto(BlacklistCensoredEvent blacklistCensoredEvent) {
        super(blacklistCensoredEvent.getServerName());
        this.playerName = blacklistCensoredEvent.getPlayer().getName();
        this.playerUuid = blacklistCensoredEvent.getPlayer().getUniqueId();
        this.censoredMessage = blacklistCensoredEvent.getCensoredMessage();
        this.originalMessage = blacklistCensoredEvent.getOriginalMessage();
        this.blacklistType = blacklistCensoredEvent.getBlacklistType();
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

    public BlacklistType getBlacklistType() {
        return blacklistType;
    }
}
