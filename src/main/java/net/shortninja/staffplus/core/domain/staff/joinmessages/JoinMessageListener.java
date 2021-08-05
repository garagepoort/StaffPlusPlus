package net.shortninja.staffplus.core.domain.staff.joinmessages;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Comparator;
import java.util.Optional;

@IocBean
@IocListener
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
        Optional<JoinMessageGroup> joinMessageGroup = getJoinMessageGroup(playerJoinEvent.getPlayer());
        joinMessageGroup.ifPresent(messageGroup -> {
            String joinMessage = messages.parse(playerJoinEvent.getPlayer(), messageGroup.getMessage().replace("%player%", playerJoinEvent.getPlayer().getName()));
            playerJoinEvent.setJoinMessage(joinMessage);
        });
    }

    private Optional<JoinMessageGroup> getJoinMessageGroup(Player player) {
        return joinMessagesConfiguration.joinMessageGroups.stream()
            .sorted(Comparator.comparingInt(JoinMessageGroup::getWeight).reversed())
            .filter(g -> permissionHandler.has(player, g.getPermission()))
            .findFirst();
    }
}
