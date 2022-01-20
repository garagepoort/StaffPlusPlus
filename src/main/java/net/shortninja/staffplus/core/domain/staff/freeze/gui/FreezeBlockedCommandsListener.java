package net.shortninja.staffplus.core.domain.staff.freeze.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.domain.staff.freeze.config.FreezeConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

@IocBean
@IocListener
public class FreezeBlockedCommandsListener implements Listener {

    @ConfigProperty("commands:freezechannel.chat")
    private String freezeChatCommand;

    private final FreezeConfiguration freezeConfiguration;
    private final OnlineSessionsManager sessionManager;

    public FreezeBlockedCommandsListener(FreezeConfiguration freezeConfiguration, OnlineSessionsManager sessionManager) {
        this.freezeConfiguration = freezeConfiguration;
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().toLowerCase();
        OnlinePlayerSession session = sessionManager.get(player);
        if(command.startsWith("/" + freezeChatCommand)) {
            return;
        }

        if (session.isFrozen() && freezeConfiguration.allowedCommands.stream().noneMatch(prefix -> command.startsWith("/" + prefix))) {
            event.setCancelled(true);
        }
    }
}