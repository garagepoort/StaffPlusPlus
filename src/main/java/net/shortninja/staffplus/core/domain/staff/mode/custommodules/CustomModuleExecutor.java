package net.shortninja.staffplus.core.domain.staff.mode.custommodules;

import org.bukkit.entity.Player;

import java.util.Map;

@FunctionalInterface
public interface CustomModuleExecutor {

    void execute(Player player, Map<String, String> placeholders);

}
