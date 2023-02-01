package net.shortninja.staffplus.core.vanish.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.vanish.VanishConfiguration;
import net.shortninja.staffplusplus.session.IPlayerSession;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@IocBukkitListener(conditionalOnProperty = "vanish-module.enabled=true")
public class TabCompleteListener implements Listener {

    private final OnlineSessionsManager onlineSessionsManager;
    private final VanishConfiguration vanishConfiguration;
    private final PermissionHandler permissionHandler;

    public TabCompleteListener(OnlineSessionsManager onlineSessionsManager, VanishConfiguration vanishConfiguration, PermissionHandler permissionHandler) {
        this.onlineSessionsManager = onlineSessionsManager;
        this.vanishConfiguration = vanishConfiguration;
        this.permissionHandler = permissionHandler;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTabComplete(TabCompleteEvent event) {
        try {
            if (!(event.getSender() instanceof Player) || permissionHandler.has(event.getSender(), vanishConfiguration.permissionSeeVanished)) {
                return;
            }

            Set<String> hiddenNames = onlineSessionsManager.getAll().stream()
                .filter(IPlayerSession::isVanished)
                .map(IPlayerSession::getName)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

            event.setCompletions(
                event.getCompletions().stream()
                    .filter(c -> !hiddenNames.contains(c.toLowerCase()))
                    .collect(Collectors.toList())
            );
        } catch (Exception e) {
            TubingBukkitPlugin.getPlugin().getLogger().warning("Could not hide autocomplete names due to: " + Arrays.toString(e.getStackTrace()));
        }
    }
}