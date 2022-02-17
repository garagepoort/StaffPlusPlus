package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.chat.mention.bungee.MentionBungeeDto;
import net.shortninja.staffplus.core.domain.chat.mention.bungee.PlayerMentionedBungeeEvent;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.alerts.config.AlertsConfiguration;
import net.shortninja.staffplusplus.chat.PlayerMentionedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean(conditionalOnProperty = "alerts-module.mention-notify-console=true")
@IocListener
public class PlayerMentionAlertConsoleHandler implements Listener {

    private final PermissionHandler permission;
    private final AlertsConfiguration alertsConfiguration;
    private final Options options;
    private final PlayerManager playerManager;

    public PlayerMentionAlertConsoleHandler(PermissionHandler permission, AlertsConfiguration alertsConfiguration, Options options, PlayerManager playerManager) {
        this.permission = permission;
        this.alertsConfiguration = alertsConfiguration;
        this.options = options;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void handle(PlayerMentionedEvent event) {
        if (permission.has(event.getPlayer(), alertsConfiguration.permissionMentionBypass)) {
            return;
        }

        extracted(event.getPlayer().getName(), event.getMentionedPlayer().getName(), options.serverName);
    }

    @EventHandler
    public void handle(PlayerMentionedBungeeEvent event) {
        MentionBungeeDto mentionBungeeDto = event.getMentionBungeeDto();
        playerManager.getOnOrOfflinePlayer(mentionBungeeDto.getPlayerUuid()).ifPresent(p -> {
            if (permission.has(p.getOfflinePlayer(), alertsConfiguration.permissionMentionBypass)) {
                return;
            }

            extracted(event.getMentionBungeeDto().getPlayerName(), event.getMentionBungeeDto().getMentionedPlayerName(), event.getMentionBungeeDto().getServerName());
        });
    }

    private void extracted(String playerName, String mentionedPlayerName, String serverName) {
        String message = "&6[%server%] &7%target% &bhas mentioned %mentioned% in chat!"
            .replace("%target%", playerName)
            .replace("%server%", serverName)
            .replace("%mentioned%", mentionedPlayerName);
        StaffPlus.get().getLogger().info(message);
    }
}
