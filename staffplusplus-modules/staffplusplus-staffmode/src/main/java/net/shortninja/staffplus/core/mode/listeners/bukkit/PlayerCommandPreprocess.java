package net.shortninja.staffplus.core.mode.listeners.bukkit;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.SplitByComma;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

@IocBukkitListener
public class PlayerCommandPreprocess implements Listener {

    @ConfigProperty("blocked-mode-commands")
    @ConfigTransformer(SplitByComma.class)
    private List<String> blockedModeCommands = new ArrayList<>();

    private final Messages messages;
    private final OnlineSessionsManager sessionManager;

    public PlayerCommandPreprocess(Messages messages,
                                   OnlineSessionsManager sessionManager) {
        this.messages = messages;
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().toLowerCase();
        OnlinePlayerSession session = sessionManager.get(player);
        if (session.isInStaffMode() && blockedModeCommands.contains(command)) {
            messages.send(player, messages.modeCommandBlocked, messages.prefixGeneral);
            event.setCancelled(true);
        }
    }
}