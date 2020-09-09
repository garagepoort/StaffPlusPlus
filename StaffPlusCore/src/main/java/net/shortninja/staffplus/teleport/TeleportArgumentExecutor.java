package net.shortninja.staffplus.teleport;

import net.shortninja.staffplus.ui.ArgumentExecutor;
import net.shortninja.staffplus.ui.ArgumentType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportArgumentExecutor implements ArgumentExecutor {
    @Override
    public boolean execute(CommandSender commandSender, String playerName, String value) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return false;
        }

        TeleportService.getInstance().teleportPlayer(commandSender, player, value);
        return true;
    }

    @Override
    public ArgumentType getType() {
        return ArgumentType.TELEPORT;
    }
}
