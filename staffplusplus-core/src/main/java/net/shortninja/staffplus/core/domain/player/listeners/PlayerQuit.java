package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@IocBukkitListener
public class PlayerQuit implements Listener {

    private final OnlineSessionsManager sessionManager;
    private final IProtocolService protocolService;
    private final PlayerSettingsRepository playerSettingsRepository;

    public PlayerQuit(OnlineSessionsManager sessionManager,
                      IProtocolService protocolService,
                      PlayerSettingsRepository playerSettingsRepository) {
        this.sessionManager = sessionManager;
        this.protocolService = protocolService;
        this.playerSettingsRepository = playerSettingsRepository;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {

        protocolService.getVersionProtocol().uninject(event.getPlayer());
        Player player = event.getPlayer();
        OnlinePlayerSession session = sessionManager.get(player);

        if (session.isVanished()) {
            event.setQuitMessage("");
        }

        playerSettingsRepository.clearSettings(player);
    }
}