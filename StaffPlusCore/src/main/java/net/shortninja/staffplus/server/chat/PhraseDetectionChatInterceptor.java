package net.shortninja.staffplus.server.chat;

import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.BukkitUtils;
import net.shortninja.staffplusplus.chat.PhrasesDetectedEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.stream.Collectors;

public class PhraseDetectionChatInterceptor implements ChatInterceptor {

    private final Options options;

    public PhraseDetectionChatInterceptor(Options options) {
        this.options = options;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        List<String> detectedPhrases = options.chatConfiguration.getDetectionPhrases().stream()
            .filter(phrase -> message.toLowerCase().contains(phrase.toLowerCase()))
            .collect(Collectors.toList());
        if (!detectedPhrases.isEmpty()) {
            BukkitUtils.sendEvent(new PhrasesDetectedEvent(options.serverName, event.getPlayer(), event.getMessage(), detectedPhrases));
        }
        return false;
    }
}
