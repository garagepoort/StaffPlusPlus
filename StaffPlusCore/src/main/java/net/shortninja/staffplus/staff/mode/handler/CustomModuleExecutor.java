package net.shortninja.staffplus.staff.mode.handler;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface CustomModuleExecutor {

    void execute(Player player);

}
