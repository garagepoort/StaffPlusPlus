package net.shortninja.staffplus.core.domain.staff.joinmessages;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigObjectList;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@IocBean
public class JoinMessagesConfiguration {

    @ConfigProperty("joinmessages-module.enabled")
    public boolean enabled;

    @ConfigProperty("joinmessages-module.messages")
    @ConfigObjectList(JoinMessageGroup.class)
    public List<JoinMessageGroup> joinMessageGroups;

    private final PermissionHandler permissionHandler;

    public JoinMessagesConfiguration(PermissionHandler permissionHandler) {
        this.permissionHandler = permissionHandler;
    }

    public Optional<JoinMessageGroup> getJoinMessageGroup(Player player) {
        return joinMessageGroups.stream()
            .sorted(Comparator.comparingInt(JoinMessageGroup::getWeight).reversed())
            .filter(g -> permissionHandler.has(player, g.getPermission()))
            .findFirst();
    }
}
