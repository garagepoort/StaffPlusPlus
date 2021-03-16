package net.shortninja.staffplus.staff.alerts.handlers;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.chat.PhrasesDetectedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatPhraseDetectedAlertHandler extends AlertsHandler implements Listener {

    public ChatPhraseDetectedAlertHandler() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler
    public void handle(PhrasesDetectedEvent phrasesDetectedEvent) {
        if (!alertsConfiguration.isAlertsChatPhraseDetectionEnabled()) {
            return;
        }

        for (Player player : getPlayersToNotify()) {
            message.send(player, messages.alertsChatPhraseDetected
                .replace("%target%", phrasesDetectedEvent.getPlayer().getName())
                .replace("%originalMessage%", phrasesDetectedEvent.getOriginalMessage())
                .replace("%detectedPhrases%", String.join(" | ", phrasesDetectedEvent.getDetectedPhrases())), messages.prefixGeneral, getPermission());
        }
    }

    @Override
    protected AlertType getType() {
        return AlertType.CHAT_PHRASE_DETECTION;
    }

    @Override
    protected String getPermission() {
        return alertsConfiguration.getPermissionChatPhraseDetection();
    }
}
