package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.chat.mention.bungee.MentionBungeeDto;
import net.shortninja.staffplus.core.domain.chat.mention.bungee.PlayerMentionedBungeeEvent;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.alerts.config.AlertsConfiguration;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.chat.PlayerMentionedEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplusplus.alerts.AlertType.MENTION;

@IocListener
public class PlayerMentionAlertHandler extends AlertsHandler implements Listener {

    private final Options options;

    public PlayerMentionAlertHandler(AlertsConfiguration alertsConfiguration,
                                     PlayerSettingsRepository playerSettingsRepository,
                                     OnlineSessionsManager sessionManager,
                                     PermissionHandler permission,
                                     Messages messages,
                                     PlayerManager playerManager, Options options) {
        super(alertsConfiguration, playerSettingsRepository, sessionManager, permission, messages, playerManager);
        this.options = options;
    }

    @EventHandler
    public void handle(PlayerMentionedEvent event) {
        if (!alertsConfiguration.alertsMentionNotify) {
            return;
        }

        if (permission.has(event.getPlayer(), alertsConfiguration.permissionMentionBypass)) {
            return;
        }

        notifyMentionedPlayer(event.getMentionedPlayer().getUniqueId(), event.getPlayer().getName(), options.serverName);
    }

    @EventHandler
    public void handle(PlayerMentionedBungeeEvent event) {
        if (!alertsConfiguration.alertsMentionNotify) {
            return;
        }

        MentionBungeeDto mentionBungeeDto = event.getMentionBungeeDto();
        Optional<SppPlayer> player = playerManager.getOnOrOfflinePlayer(mentionBungeeDto.getPlayerUuid());
        if (player.isPresent() && permission.has(player.get().getOfflinePlayer(), alertsConfiguration.permissionMentionBypass)) {
            return;
        }
        notifyMentionedPlayer(mentionBungeeDto.getMentionedPlayerUuid(), mentionBungeeDto.getPlayerName(), mentionBungeeDto.getServerName());
    }

    private void notifyMentionedPlayer(UUID mentionedPlayerUuid, String playerName, String serverName) {
        Optional<SppPlayer> onlinePlayer = playerManager.getOnlinePlayer(mentionedPlayerUuid);

        onlinePlayer.ifPresent(mentioned -> {
            Player mentionedPlayer = mentioned.getPlayer();
            PlayerSettings session = playerSettingsRepository.get(mentionedPlayer);
            if (session.getAlertOptions().contains(MENTION) && permission.has(mentionedPlayer, alertsConfiguration.permissionMention)) {
                messages.send(mentionedPlayer, messages.alertsMention
                    .replace("%server%", serverName)
                    .replace("%target%", playerName), messages.prefixGeneral, alertsConfiguration.permissionMention);
                if (alertsConfiguration.alertsSound != null) {
                    alertsConfiguration.alertsSound.play(mentionedPlayer);
                }
            }
        });
    }

    @Override
    protected AlertType getType() {
        return MENTION;
    }

    @Override
    protected String getPermission() {
        return alertsConfiguration.permissionMention;
    }
}
