package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.chat.mention.bungee.MentionBungeeDto;
import net.shortninja.staffplus.core.domain.chat.mention.bungee.PlayerMentionedBungeeEvent;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.chat.PlayerMentionedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBukkitListener(conditionalOnProperty = "alerts-module.mention-notify-console=true")
public class PlayerMentionAlertConsoleHandler implements Listener {

    private final Options options;
    private final PlayerManager playerManager;

    public PlayerMentionAlertConsoleHandler(Options options, PlayerManager playerManager) {
        this.options = options;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void handle(PlayerMentionedEvent event) {
        extracted(event.getPlayer().getName(), event.getMentionedPlayer().getName(), options.serverName);
    }

    @EventHandler
    public void handle(PlayerMentionedBungeeEvent event) {
        MentionBungeeDto mentionBungeeDto = event.getMentionBungeeDto();
        playerManager.getOnOrOfflinePlayer(mentionBungeeDto.getPlayerUuid())
            .ifPresent(p -> extracted(event.getMentionBungeeDto().getPlayerName(), event.getMentionBungeeDto().getMentionedPlayerName(), event.getMentionBungeeDto().getServerName()));
    }

    private void extracted(String playerName, String mentionedPlayerName, String serverName) {
        String message = "&6[%server%] &7%target% &bhas mentioned %mentioned% in chat!"
            .replace("%target%", playerName)
            .replace("%server%", serverName)
            .replace("%mentioned%", mentionedPlayerName);
        TubingBukkitPlugin.getPlugin().getLogger().info(message);
    }
}
