package net.shortninja.staffplus.server.command.arguments;

import net.shortninja.staffplus.player.StripService;
import net.shortninja.staffplus.server.command.arguments.ArgumentExecutor;
import net.shortninja.staffplus.server.command.arguments.ArgumentType;
import net.shortninja.staffplus.teleport.TeleportService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StripArgumentExecutor implements ArgumentExecutor {
    @Override
    public boolean execute(CommandSender commandSender, String playerName, String value) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return false;
        }

        StripService.getInstance().strip(commandSender, player);
        return true;
    }

    @Override
    public ArgumentType getType() {
        return ArgumentType.STRIP;
    }
}
