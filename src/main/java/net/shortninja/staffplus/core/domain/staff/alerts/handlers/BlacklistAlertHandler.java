package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.blacklist.bungee.ChatMessageCensoredBungeeDto;
import net.shortninja.staffplus.core.domain.blacklist.bungee.ChatMessageCensoredBungeeEvent;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.alerts.config.AlertsConfiguration;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.blacklist.BlacklistCensoredEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBukkitListener(conditionalOnProperty = "alerts-module.blacklist-detection=true")
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
    public void handle(BlacklistCensoredEvent blacklistCensoredEvent) {

        for (Player player : getPlayersToNotify()) {
            String message = messages.alertsBlacklist
                .replace("%player%", blacklistCensoredEvent.getPlayer().getName())
                .replace("%uncensored-message%", blacklistCensoredEvent.getOriginalMessage())
                .replace("%censored-message%", blacklistCensoredEvent.getCensoredMessage())
                .replace("%blacklist-type%", blacklistCensoredEvent.getBlacklistType().name())
                .replace("%server%", blacklistCensoredEvent.getServerName());
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
                .replace("%blacklist-type%", chatMessageCensoredBungeeDto.getBlacklistType().name())
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
