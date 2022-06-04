package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.chat.blacklist.bungee.ChatMessageCensoredBungeeDto;
import net.shortninja.staffplus.core.domain.chat.blacklist.bungee.ChatMessageCensoredBungeeEvent;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.alerts.config.AlertsConfiguration;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.chat.ChatMessageCensoredEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocListener(conditionalOnProperty = "alerts-module.blacklist-detection=true")
public class BlacklistAlertHandler extends AlertsHandler implements Listener {

    public BlacklistAlertHandler(AlertsConfiguration alertsConfiguration,
                                 OnlineSessionsManager sessionManager,
                                 PlayerSettingsRepository playerSettingsRepository,
                                 PermissionHandler permission,
                                 Messages messages,
                                 PlayerManager playerManager) {
        super(alertsConfiguration, playerSettingsRepository, sessionManager, permission, messages, playerManager);
    }

    @EventHandler
    public void handle(ChatMessageCensoredEvent chatMessageCensoredEvent) {

        for (Player player : getPlayersToNotify()) {
            String message = messages.alertsBlacklist
                .replace("%player%", chatMessageCensoredEvent.getPlayer().getName())
                .replace("%uncensored-message%", chatMessageCensoredEvent.getOriginalMessage())
                .replace("%censored-message%", chatMessageCensoredEvent.getCensoredMessage())
                .replace("%server%", chatMessageCensoredEvent.getServerName());
            messages.send(player, message, messages.prefixGeneral, getPermission());
        }
    }

    @EventHandler
    public void handle(ChatMessageCensoredBungeeEvent event) {
        ChatMessageCensoredBungeeDto chatMessageCensoredBungeeDto = event.getChatMessageCensoredBungeeDto();
        for (Player player : getPlayersToNotify()) {
            String message = messages.alertsBlacklist
                .replace("%player%", chatMessageCensoredBungeeDto.getPlayerName())
                .replace("%uncensored-message%", chatMessageCensoredBungeeDto.getOriginalMessage())
                .replace("%censored-message%", chatMessageCensoredBungeeDto.getCensoredMessage())
                .replace("%server%", chatMessageCensoredBungeeDto.getServerName());
            messages.send(player, message, messages.prefixGeneral, getPermission());
        }
    }

    @Override
    protected AlertType getType() {
        return AlertType.BLACKLIST;
    }

    @Override
    protected String getPermission() {
        return alertsConfiguration.permissionBlacklistDetection;
    }
}
