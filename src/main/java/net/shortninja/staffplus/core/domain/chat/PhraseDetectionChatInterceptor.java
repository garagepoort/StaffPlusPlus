package net.shortninja.staffplus.core.domain.chat;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplusplus.chat.PhrasesDetectedEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.stream.Collectors;

@IocBean
@IocMultiProvider(ChatInterceptor.class)
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
