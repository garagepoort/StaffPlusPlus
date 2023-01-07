package net.shortninja.staffplus.core.domain.chat.bungee;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PhraseDetectedBungeeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final PhraseDetectedBungeeDto phraseDetectedBungeeDto;

    public PhraseDetectedBungeeEvent(PhraseDetectedBungeeDto phraseDetectedBungeeDto) {
        this.phraseDetectedBungeeDto = phraseDetectedBungeeDto;
    }

    public PhraseDetectedBungeeDto getPhraseDetectedBungeeDto() {
        return phraseDetectedBungeeDto;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
