package net.shortninja.staffplus.staff.teleport.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.server.command.arguments.ArgumentType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.server.command.arguments.ArgumentType.HEALTH;
import static net.shortninja.staffplus.server.command.arguments.ArgumentType.STRIP;

public class TeleportToLocationCmd extends AbstractCmd {

    public TeleportToLocationCmd(String name) {
        super(name, IocContainer.getOptions().permissionTeleportToLocation);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer) {
        String locationName = args[1];

        IocContainer.getTeleportService().teleportPlayerToLocation(sender, targetPlayer.getPlayer(), locationName);
        return true;
    }

    @Override
    protected List<ArgumentType> getPostExecutionSppArguments() {
        return Arrays.asList(STRIP, HEALTH);
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.ofNullable(args[0]);
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 2;
    }

    @Override
    protected boolean isDelayable() {
        return true;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.ONLINE;
    }

    @Override
    protected boolean canBypass(Player player) {
        return permission.has(player, options.permissionTeleportBypass);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }
        if (args.length == 2) {
            IocContainer.getOptions().locations.forEach((k, v) -> {
                suggestions.add(k);
            });
            return suggestions.stream()
                .filter(s -> args[1].isEmpty() || s.contains(args[1]))
                .collect(Collectors.toList());
        }

        return getSppArgumentsSuggestions(sender, args);
    }
}