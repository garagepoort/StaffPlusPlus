package net.shortninja.staffplus.core.freeze.listeners;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.freeze.FreezeHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import net.shortninja.staffplus.core.freeze.config.FreezeConfiguration;

@IocBukkitListener
public class PlayerQuit implements Listener {

    @ConfigProperty("permissions:freeze")
    private String permissionFreeze;

    private final Messages messages;
    private final OnlineSessionsManager sessionManager;
    private final FreezeConfiguration freezeConfiguration;
    private final FreezeHandler freezeHandler;

    public PlayerQuit(Messages messages,
                      OnlineSessionsManager sessionManager,
                      FreezeConfiguration freezeConfiguration,
                      FreezeHandler freezeHandler) {
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.freezeConfiguration = freezeConfiguration;
        this.freezeHandler = freezeHandler;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        OnlinePlayerSession session = sessionManager.get(player);

        if (session.isFrozen()) {
            for (String command : freezeConfiguration.logoutCommands) {
                command = command.replace("%player%", player.getName());
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            }
            freezeHandler.removeFreeze(Bukkit.getConsoleSender(), player);
        }

        if (session.isFrozen()) {
            messages.sendGroupMessage(messages.freezeLogout.replace("%player%", player.getName()), permissionFreeze, messages.prefixGeneral);
        }
    }
}