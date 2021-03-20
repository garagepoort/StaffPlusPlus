package net.shortninja.staffplus.domain.staff.teleport;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.cmd.arguments.ArgumentExecutor;
import net.shortninja.staffplus.common.cmd.arguments.ArgumentType;
import net.shortninja.staffplus.common.config.Options;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeleportArgumentExecutor implements ArgumentExecutor {

    private final Options options = IocContainer.getOptions();

    @Override
    public boolean execute(CommandSender commandSender, String playerName, String value) {
        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) {
            return false;
        }

        IocContainer.getTeleportService().teleportPlayerToLocation(commandSender, player, value);
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
