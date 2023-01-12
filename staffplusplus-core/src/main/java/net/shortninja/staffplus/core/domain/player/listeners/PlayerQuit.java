package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.core.domain.staff.freeze.config.FreezeConfiguration;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@IocBukkitListener
public class PlayerQuit implements Listener {

    @ConfigProperty("permissions:freeze")
    private String permissionFreeze;

    private final Messages messages;
    private final OnlineSessionsManager sessionManager;
    private final TraceService traceService;
    private final IProtocolService protocolService;
    private final FreezeConfiguration freezeConfiguration;
    private final PlayerSettingsRepository playerSettingsRepository;
    private final FreezeHandler freezeHandler;

    public PlayerQuit(Messages messages,
                      OnlineSessionsManager sessionManager,
                      TraceService traceService,
                      IProtocolService protocolService,
                      FreezeConfiguration freezeConfiguration,
                      PlayerSettingsRepository playerSettingsRepository,
                      FreezeHandler freezeHandler) {
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.traceService = traceService;
        this.protocolService = protocolService;
        this.freezeConfiguration = freezeConfiguration;
        this.playerSettingsRepository = playerSettingsRepository;
        this.freezeHandler = freezeHandler;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {

        protocolService.getVersionProtocol().uninject(event.getPlayer());
        Player player = event.getPlayer();
        OnlinePlayerSession session = sessionManager.get(player);

        if (session.isVanished()) {
            event.setQuitMessage("");
        }

        if (session.isFrozen()) {
            for (String command : freezeConfiguration.logoutCommands) {
                command = command.replace("%player%", player.getName());
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            }
            freezeHandler.removeFreeze(Bukkit.getConsoleSender(), player);
        }

        traceService.sendTraceMessage(player.getUniqueId(), "Left the game");
        traceService.stopAllTracesForPlayer(player.getUniqueId());

        if (session.isFrozen()) {
            messages.sendGroupMessage(messages.freezeLogout.replace("%player%", player.getName()), permissionFreeze, messages.prefixGeneral);
        }

        playerSettingsRepository.clearSettings(player);
    }
}