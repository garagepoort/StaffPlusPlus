package net.shortninja.staffplus.core.alerts.handlers;

import be.garagepoort.mcioc.TubingPlugin;
import be.garagepoort.mcioc.load.InjectTubingPlugin;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplusplus.blacklist.BlacklistCensoredEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import net.shortninja.staffplus.core.blacklist.bungee.ChatMessageCensoredBungeeEvent;
import net.shortninja.staffplus.core.blacklist.bungee.ChatMessageCensoredBungeeDto;

@IocBukkitListener(conditionalOnProperty = "alerts-module.blacklist-detection-console=true")
public class BlacklistAlertConsoleHandler implements Listener {

    private final Messages messages;
    private final TubingPlugin tubingPlugin;

    public BlacklistAlertConsoleHandler(Messages messages, @InjectTubingPlugin TubingPlugin tubingPlugin) {
        this.messages = messages;
        this.tubingPlugin = tubingPlugin;
    }

    @EventHandler
    public void handle(BlacklistCensoredEvent blacklistCensoredEvent) {
        String message = messages.alertsBlacklist
            .replace("%player%", blacklistCensoredEvent.getPlayer().getName())
            .replace("%uncensored-message%", blacklistCensoredEvent.getOriginalMessage())
            .replace("%censored-message%", blacklistCensoredEvent.getCensoredMessage())
            .replace("%blacklist-type%", blacklistCensoredEvent.getBlacklistType().name())
            .replace("%server%", blacklistCensoredEvent.getServerName());

        tubingPlugin.getLogger().info(message);
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

        tubingPlugin.getLogger().info(message);
    }
}
