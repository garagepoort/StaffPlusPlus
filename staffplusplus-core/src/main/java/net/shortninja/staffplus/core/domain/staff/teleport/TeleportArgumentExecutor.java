package net.shortninja.staffplus.core.domain.staff.teleport;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentExecutor;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType;
import net.shortninja.staffplus.core.domain.staff.teleport.config.TeleportConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@IocBean
@IocMultiProvider(ArgumentExecutor.class)
public class TeleportArgumentExecutor implements ArgumentExecutor {

    private final TeleportService teleportService;
    private final TeleportConfiguration teleportConfiguration;

    public TeleportArgumentExecutor(TeleportService teleportService, TeleportConfiguration teleportConfiguration) {
        this.teleportService = teleportService;
        this.teleportConfiguration = teleportConfiguration;
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
    public List<String> complete(String currentArg) {
        List<String> suggestions = new ArrayList<>();
        teleportConfiguration.locations.forEach((k,v) -> {
            suggestions.add(getType().getPrefix() + k);
        });
        return suggestions;
    }
}
