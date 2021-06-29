package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplusplus.chat.PhrasesDetectedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean(conditionalOnProperty = "alerts-module.chat-phrase-detection-console=true")
@IocListener
public class ChatPhraseDetectedAlertConsoleHandler implements Listener {

    private final Messages messages;
    private final PermissionHandler permissionHandler;
    private final Options options;

    public ChatPhraseDetectedAlertConsoleHandler(Messages messages, PermissionHandler permissionHandler, Options options) {
        this.messages = messages;
        this.permissionHandler = permissionHandler;
        this.options = options;
    }

    @EventHandler
    public void handle(PhrasesDetectedEvent phrasesDetectedEvent) {
        if (permissionHandler.has(phrasesDetectedEvent.getPlayer(), options.alertsConfiguration.getPermissionChatPhraseDetectionBypass())) {
            return;
        }

        String message = messages.alertsChatPhraseDetected
            .replace("%target%", phrasesDetectedEvent.getPlayer().getName())
            .replace("%originalMessage%", phrasesDetectedEvent.getOriginalMessage())
            .replace("%detectedPhrases%", String.join(" | ", phrasesDetectedEvent.getDetectedPhrases()));

        StaffPlus.get().getLogger().info(message);
    }

}
