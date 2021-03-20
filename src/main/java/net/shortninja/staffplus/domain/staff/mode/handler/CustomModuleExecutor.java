package net.shortninja.staffplus.domain.staff.mode.handler;

import org.bukkit.entity.Player;

import java.util.Map;

@FunctionalInterface
public interface CustomModuleExecutor {

    void execute(Player player, Map<String, String> placeholders);

}
