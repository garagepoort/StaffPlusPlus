package net.shortninja.staffplus.core.domain.staff.teleport;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentExecutor;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType;
import net.shortninja.staffplus.core.common.config.Options;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@IocBean
@IocMultiProvider(ArgumentExecutor.class)
public class TeleportArgumentExecutor implements ArgumentExecutor {

    private final Options options;
    private final TeleportService teleportService;

    public TeleportArgumentExecutor(Options options, TeleportService teleportService) {
        this.options = options;
        this.teleportService = teleportService;
    }

    @Override
    public boolean execute(CommandSender commandSender, String playerName, String value) {
        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) {
            return false;
        }

        teleportService.teleportPlayerToLocation(commandSender, player, value);
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
