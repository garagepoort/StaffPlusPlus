package net.shortninja.staffplus.core.domain.chat;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.chat.configuration.ChatConfiguration;
import net.shortninja.staffplusplus.chat.PhrasesDetectedEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.stream.Collectors;

@IocBean
@IocMultiProvider(ChatInterceptor.class)
public class PhraseDetectionChatInterceptor implements ChatInterceptor {

    private final Options options;
    private final ChatConfiguration chatConfiguration;

    public PhraseDetectionChatInterceptor(Options options, ChatConfiguration chatConfiguration) {
        this.options = options;
        this.chatConfiguration = chatConfiguration;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        List<String> detectedPhrases = chatConfiguration.detectionPhrases.stream()
            .filter(phrase -> message.toLowerCase().contains(phrase.toLowerCase()))
            .collect(Collectors.toList());
        if (!detectedPhrases.isEmpty()) {
            BukkitUtils.sendEvent(new PhrasesDetectedEvent(options.serverName, event.getPlayer(), event.getMessage(), detectedPhrases));
        }
        return false;
    }

    @Override
    public int getPriority() {
        return 7;
    }
}
