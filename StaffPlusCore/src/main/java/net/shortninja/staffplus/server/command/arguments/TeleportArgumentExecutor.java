package net.shortninja.staffplus.server.command.arguments;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.teleport.TeleportService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeleportArgumentExecutor implements ArgumentExecutor {

    private final Options options = StaffPlus.get().options;

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

    @Override
    public List<String> complete(CommandSender sender, String currentArg) {
        List<String> suggestions = new ArrayList<>();
        options.locations.forEach((k,v) -> {
            suggestions.add(getType().getPrefix() + k);
        });
        return suggestions;
    }
}
