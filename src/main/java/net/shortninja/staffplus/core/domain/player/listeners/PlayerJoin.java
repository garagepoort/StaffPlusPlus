package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.StaffPlusPlusJoinedEvent;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocListener
public class PlayerJoin implements Listener {

    private final PlayerSettingsRepository playerSettingsRepository;
    private final OnlineSessionsManager sessionManager;
    private final PlayerManager playerManager;
    private final IProtocolService protocolService;
    private final BukkitUtils bukkitUtils;

    public PlayerJoin(PlayerSettingsRepository playerSettingsRepository,
                      OnlineSessionsManager sessionManager,
                      PlayerManager playerManager,
                      IProtocolService protocolService,
                      BukkitUtils bukkitUtils) {
        this.playerSettingsRepository = playerSettingsRepository;
        this.sessionManager = sessionManager;
        this.playerManager = playerManager;
        this.protocolService = protocolService;
        this.bukkitUtils = bukkitUtils;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        protocolService.getVersionProtocol().inject(event.getPlayer());
        playerManager.syncPlayer(event.getPlayer());
        Player player = event.getPlayer();
        bukkitUtils.runTaskAsync(() -> {
            playerManager.storePlayer(player);
            PlayerSettings playerSettings = playerSettingsRepository.get(player);
            OnlinePlayerSession onlinePlayerSession = sessionManager.get(player);
            sendEvent(new StaffPlusPlusJoinedEvent(event, onlinePlayerSession, playerSettings));
        });
    }
}