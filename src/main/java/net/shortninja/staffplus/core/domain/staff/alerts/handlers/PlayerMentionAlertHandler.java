package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.alerts.config.AlertsConfiguration;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.chat.PlayerMentionedEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

import static net.shortninja.staffplusplus.alerts.AlertType.MENTION;

@IocBean
public class PlayerMentionAlertHandler extends AlertsHandler implements Listener {

    public PlayerMentionAlertHandler(AlertsConfiguration alertsConfiguration,
                                     PlayerSettingsRepository playerSettingsRepository,
                                     OnlineSessionsManager sessionManager,
                                     PermissionHandler permission,
                                     Messages messages,
                                     PlayerManager playerManager) {
        super(alertsConfiguration, playerSettingsRepository, sessionManager, permission, messages, playerManager);
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler
    public void handle(PlayerMentionedEvent event) {
        if (!alertsConfiguration.alertsMentionNotify) {
            return;
        }

        if (permission.has(event.getPlayer(), alertsConfiguration.permissionMentionBypass)) {
            return;
        }

        if (event.getMentionedPlayer().isOnline()) {
            Optional<SppPlayer> onlinePlayer = playerManager.getOnlinePlayer(event.getMentionedPlayer().getUniqueId());

            onlinePlayer.ifPresent(p -> {
                Player mentionedPlayer = p.getPlayer();
                PlayerSettings session = playerSettingsRepository.get(mentionedPlayer);
                if (session.getAlertOptions().contains(MENTION) && permission.has(mentionedPlayer, alertsConfiguration.permissionMention)) {
                    messages.send(mentionedPlayer, messages.alertsMention.replace("%target%", event.getPlayer().getName()), messages.prefixGeneral, alertsConfiguration.permissionMention);
                    if (alertsConfiguration.alertsSound != null) {
                        alertsConfiguration.alertsSound.play(mentionedPlayer);
                    }
                }
            });
        }

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
