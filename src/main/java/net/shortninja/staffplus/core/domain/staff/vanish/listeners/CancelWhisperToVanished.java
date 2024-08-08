package net.shortninja.staffplus.core.domain.staff.vanish.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

@IocBukkitListener(conditionalOnProperty = "vanish-module.enabled=true")
public class CancelWhisperToVanished implements Listener {

    private final OnlineSessionsManager sessionManager;
    private final PlayerManager playerManager;
    private final Messages messages;
    private final VanishConfiguration vanishConfiguration;
    private final PermissionHandler permissionHandler;

    public CancelWhisperToVanished(OnlineSessionsManager sessionManager, PlayerManager playerManager, Messages messages, VanishConfiguration vanishConfiguration, PermissionHandler permissionHandler) {
        this.sessionManager = sessionManager;
        this.playerManager = playerManager;
        this.messages = messages;
        this.vanishConfiguration = vanishConfiguration;
        this.permissionHandler = permissionHandler;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void interceptCommands(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().toLowerCase();
        if(!command.startsWith("/msg ") && !command.startsWith("/tell ")) {
            return;
        }

        if(permissionHandler.has(event.getPlayer(), vanishConfiguration.permissionSeeVanished)) {
            return;
        }

        String[] args = command.split(" ");
        if(args.length < 3) {
            return;
        }

        String targetPlayerName = args[1];

        playerManager.getOnlinePlayer(targetPlayerName)
            .map(sppPlayer -> sessionManager.get(sppPlayer.getPlayer()))
            .filter(PlayerSession::isVanished)
            .ifPresent(session -> {
                messages.send(event.getPlayer(), "&cNo player was found", "");
                event.setCancelled(true);
            });
    }
}
