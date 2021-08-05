package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.chat.NameChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

@IocBean
public class PlayerJoin implements Listener {

    @ConfigProperty("permissions:mode")
    private String permissionMode;

    private final Options options;
    private final SessionManagerImpl sessionManager;
    private final PlayerManager playerManager;
    private final IProtocolService protocolService;

    public PlayerJoin(Options options,
                      SessionManagerImpl sessionManager,
                      PlayerManager playerManager,
                      IProtocolService protocolService) {
        this.options = options;
        this.sessionManager = sessionManager;
        this.playerManager = playerManager;
        this.protocolService = protocolService;
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        protocolService.getVersionProtocol().inject(event.getPlayer());
        playerManager.syncPlayer(event.getPlayer());

        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> manageUser(player));
    }

    private void manageUser(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerSession playerSession = sessionManager.get(uuid);

        if (!playerSession.getName().equals(player.getName())) {
            BukkitUtils.sendEvent(new NameChangeEvent(options.serverName, player, playerSession.getName(), player.getName()));
            playerSession.setName(player.getName());
            sessionManager.saveSession(player);
        }
    }
}