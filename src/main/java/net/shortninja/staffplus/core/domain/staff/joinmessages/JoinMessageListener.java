package net.shortninja.staffplus.core.domain.staff.joinmessages;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

@IocBukkitListener(conditionalOnProperty = "joinmessages-module.enabled=true")
public class JoinMessageListener implements Listener {

    private final JoinMessagesConfiguration joinMessagesConfiguration;
    private final PermissionHandler permissionHandler;
    private final Messages messages;

    public JoinMessageListener(JoinMessagesConfiguration joinMessagesConfiguration, PermissionHandler permissionHandler, Messages messages) {
        this.joinMessagesConfiguration = joinMessagesConfiguration;
        this.permissionHandler = permissionHandler;
        this.messages = messages;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent playerJoinEvent) {
        Optional<JoinMessageGroup> joinMessageGroup = joinMessagesConfiguration.getJoinMessageGroup(playerJoinEvent.getPlayer());
        joinMessageGroup.ifPresent(messageGroup -> {
            String joinMessage = messages.parse(playerJoinEvent.getPlayer(), messageGroup.getMessage().replace("%player%", playerJoinEvent.getPlayer().getName()));
            playerJoinEvent.setJoinMessage(joinMessage);
        });
    }
}
