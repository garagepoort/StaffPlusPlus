package net.shortninja.staffplus.core.domain.chat.bungee;

import net.shortninja.staffplus.core.common.bungee.BungeeMessage;
import net.shortninja.staffplusplus.chat.PhrasesDetectedEvent;

import java.util.List;
import java.util.UUID;

public class PhraseDetectedBungeeDto extends BungeeMessage {

    private final UUID playerUuid;
    private final String playerName;
    private final String originalMessage;
    private final List<String> detectedPhrases;

    public PhraseDetectedBungeeDto(PhrasesDetectedEvent phrasesDetectedEvent) {
        super(phrasesDetectedEvent.getServerName());
        this.playerName = phrasesDetectedEvent.getPlayer().getName();
        this.playerUuid = phrasesDetectedEvent.getPlayer().getUniqueId();
        this.detectedPhrases = phrasesDetectedEvent.getDetectedPhrases();
        this.originalMessage = phrasesDetectedEvent.getOriginalMessage();
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public List<String> getDetectedPhrases() {
        return detectedPhrases;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }
}
