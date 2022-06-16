package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.domain.blacklist.bungee.ChatMessageCensoredBungeeDto;
import net.shortninja.staffplus.core.domain.blacklist.bungee.ChatMessageCensoredBungeeEvent;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.chat.BlacklistCensoredEvent;
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
    public void handle(BlacklistCensoredEvent blacklistCensoredEvent) {
        String message = messages.alertsBlacklist
            .replace("%player%", blacklistCensoredEvent.getPlayer().getName())
            .replace("%uncensored-message%", blacklistCensoredEvent.getOriginalMessage())
            .replace("%censored-message%", blacklistCensoredEvent.getCensoredMessage())
            .replace("%blacklist-type%", blacklistCensoredEvent.getBlacklistType().name())
            .replace("%server%", blacklistCensoredEvent.getServerName());

        StaffPlus.get().getLogger().info(message);
    }

    @EventHandler
    public void handleBungee(ChatMessageCensoredBungeeEvent chatMessageCensoredEvent) {
        ChatMessageCensoredBungeeDto chatMessageCensoredBungeeDto = chatMessageCensoredEvent.getChatMessageCensoredBungeeDto();
        String message = messages.alertsBlacklist
            .replace("%player%", chatMessageCensoredBungeeDto.getPlayerName())
            .replace("%uncensored-message%", chatMessageCensoredBungeeDto.getOriginalMessage())
            .replace("%censored-message%", chatMessageCensoredBungeeDto.getCensoredMessage())
            .replace("%blacklist-type%", chatMessageCensoredBungeeDto.getBlacklistType().name())
            .replace("%server%", chatMessageCensoredBungeeDto.getServerName());

        StaffPlus.get().getLogger().info(message);
    }
}
