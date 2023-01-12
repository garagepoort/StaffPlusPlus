package net.shortninja.staffplus.core.alerts.handlers;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.chat.bungee.PhraseDetectedBungeeDto;
import net.shortninja.staffplus.core.domain.chat.bungee.PhraseDetectedBungeeEvent;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.alerts.config.AlertsConfiguration;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.chat.PhrasesDetectedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

@IocBukkitListener(conditionalOnProperty = "alerts-module.chat-phrase-detection=true")
public class ChatPhraseDetectedAlertHandler extends AlertsHandler implements Listener {

    public ChatPhraseDetectedAlertHandler(AlertsConfiguration alertsConfiguration,
                                          OnlineSessionsManager sessionManager,
                                          PlayerSettingsRepository playerSettingsRepository,
                                          PermissionHandler permission,
                                          Messages messages,
                                          PlayerManager playerManager) {
        super(alertsConfiguration, playerSettingsRepository, sessionManager, permission, messages, playerManager);
    }

    @EventHandler
    public void handle(PhrasesDetectedEvent phrasesDetectedEvent) {
        notifyPlayers(phrasesDetectedEvent.getPlayer().getName(), phrasesDetectedEvent.getOriginalMessage(), phrasesDetectedEvent.getDetectedPhrases(), phrasesDetectedEvent.getServerName());
    }

    @EventHandler
    public void handle(PhraseDetectedBungeeEvent phraseDetectedBungeeEvent) {
        PhraseDetectedBungeeDto detectedBungeeDto = phraseDetectedBungeeEvent.getPhraseDetectedBungeeDto();
        notifyPlayers(detectedBungeeDto.getPlayerName(), detectedBungeeDto.getOriginalMessage(), detectedBungeeDto.getDetectedPhrases(), detectedBungeeDto.getServerName());
    }

    private void notifyPlayers(String playerName, String originalMessage, List<String> detectedPhrases, String serverName) {
        for (Player player : getPlayersToNotify()) {
            messages.send(player, messages.alertsChatPhraseDetected
                .replace("%target%", playerName)
                .replace("%server%", serverName)
                .replace("%originalMessage%", originalMessage)
                .replace("%detectedPhrases%", String.join(" | ", detectedPhrases)), messages.prefixGeneral, getPermission());
        }
    }

    @Override
    protected AlertType getType() {
        return AlertType.CHAT_PHRASE_DETECTION;
    }

    @Override
    protected String getPermission() {
        return alertsConfiguration.permissionChatPhraseDetection;
    }
}
