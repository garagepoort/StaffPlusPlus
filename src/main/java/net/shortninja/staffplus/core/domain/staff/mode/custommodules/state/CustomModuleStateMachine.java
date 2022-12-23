package net.shortninja.staffplus.core.domain.staff.mode.custommodules.state;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@IocBean
public class CustomModuleStateMachine {

    private static final Map<UUID, Set<String>> activeStates = new HashMap<>();

    public void switchState(Player player, String fromState, String toState) {
        activeStates.putIfAbsent(player.getUniqueId(), new HashSet<>());
        activeStates.get(player.getUniqueId()).remove(fromState);
        activeStates.get(player.getUniqueId()).add(toState);
        BukkitUtils.sendEvent(new ModuleStateChangedEvent(player.getUniqueId()));
    }

    public boolean isActive(Player player, String state) {
        return activeStates.getOrDefault(player.getUniqueId(), new HashSet<>()).contains(state);
    }

    public void initState(Player player, Set<String> initialItemStates) {
        activeStates.put(player.getUniqueId(), initialItemStates);
    }

    public void clearState(Player player) {
        activeStates.put(player.getUniqueId(), new HashSet<>());
    }
}
