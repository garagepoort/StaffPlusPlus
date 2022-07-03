package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.TubingPlugin;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.domain.chat.bungee.PhraseDetectedBungeeDto;
import net.shortninja.staffplus.core.domain.chat.bungee.PhraseDetectedBungeeEvent;
import net.shortninja.staffplusplus.chat.PhrasesDetectedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

@IocBukkitListener(conditionalOnProperty = "alerts-module.chat-phrase-detection-console=true")
public class ChatPhraseDetectedAlertConsoleHandler implements Listener {

    private final Messages messages;
    private final TubingPlugin tubingPlugin;

    public ChatPhraseDetectedAlertConsoleHandler(Messages messages, TubingPlugin tubingPlugin) {
        this.messages = messages;
        this.tubingPlugin = tubingPlugin;
    }

    @EventHandler
    public void handle(PhrasesDetectedEvent phrasesDetectedEvent) {
        log(phrasesDetectedEvent.getPlayer().getName(), phrasesDetectedEvent.getOriginalMessage(), phrasesDetectedEvent.getDetectedPhrases(), phrasesDetectedEvent.getServerName());
    }

    @EventHandler
    public void handle(PhraseDetectedBungeeEvent phraseDetectedBungeeEvent) {
        PhraseDetectedBungeeDto phraseDetectedBungeeDto = phraseDetectedBungeeEvent.getPhraseDetectedBungeeDto();
        log(phraseDetectedBungeeDto.getPlayerName(), phraseDetectedBungeeDto.getOriginalMessage(), phraseDetectedBungeeDto.getDetectedPhrases(), phraseDetectedBungeeDto.getServerName());
    }

    private void log(String target, String originalMessage, List<String> detectedPhrases, String serverName) {
        String message = messages.alertsChatPhraseDetected
            .replace("%target%", target)
            .replace("%server%", serverName)
            .replace("%originalMessage%", originalMessage)
            .replace("%detectedPhrases%", String.join(" | ", detectedPhrases));

        tubingPlugin.getLogger().info(message);
    }
}
