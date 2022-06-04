package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.domain.chat.blacklist.bungee.ChatMessageCensoredBungeeDto;
import net.shortninja.staffplus.core.domain.chat.blacklist.bungee.ChatMessageCensoredBungeeEvent;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.chat.ChatMessageCensoredEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocListener(conditionalOnProperty = "alerts-module.blacklist-detection-console=true")
public class BlacklistAlertConsoleHandler implements Listener {

    private final Messages messages;
    private final PlayerManager playerManager;

    public BlacklistAlertConsoleHandler(Messages messages, PlayerManager playerManager) {
        this.messages = messages;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void handle(ChatMessageCensoredEvent chatMessageCensoredEvent) {
        String message = messages.alertsBlacklist
            .replace("%player%", chatMessageCensoredEvent.getPlayer().getName())
            .replace("%uncensored-message%", chatMessageCensoredEvent.getOriginalMessage())
            .replace("%censored-message%", chatMessageCensoredEvent.getCensoredMessage())
            .replace("%server%", chatMessageCensoredEvent.getServerName());

        StaffPlus.get().getLogger().info(message);
    }

    @EventHandler
    public void handleBungee(ChatMessageCensoredBungeeEvent chatMessageCensoredEvent) {
        ChatMessageCensoredBungeeDto chatMessageCensoredBungeeDto = chatMessageCensoredEvent.getChatMessageCensoredBungeeDto();
        String message = messages.alertsBlacklist
            .replace("%player%", chatMessageCensoredBungeeDto.getPlayerName())
            .replace("%uncensored-message%", chatMessageCensoredBungeeDto.getOriginalMessage())
            .replace("%censored-message%", chatMessageCensoredBungeeDto.getCensoredMessage())
            .replace("%server%", chatMessageCensoredBungeeDto.getServerName());

        StaffPlus.get().getLogger().info(message);
    }
}
