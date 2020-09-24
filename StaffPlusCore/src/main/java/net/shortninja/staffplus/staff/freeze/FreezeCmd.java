package net.shortninja.staffplus.staff.freeze;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.server.command.arguments.ArgumentType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.server.command.PlayerRetrievalStrategy.ONLINE;
import static net.shortninja.staffplus.server.command.arguments.ArgumentType.*;

public class FreezeCmd extends AbstractCmd {
    private static final List<ArgumentType> VALID_ARGUMENTS = Arrays.asList(TELEPORT, STRIP, HEALTH);
    private static final String ENABLED = "enabled";
    private static final String DISABLED = "disabled";

    private final FreezeHandler freezeHandler = IocContainer.getFreezeHandler();

    public FreezeCmd(String name) {
        super(name, IocContainer.getOptions().permissionFreeze);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer sppPlayer) {
        argumentProcessor.parseArguments(sender, getPlayerName(sender, args).get(), getSppArguments(sender, args), VALID_ARGUMENTS);
        freezeHandler.execute(buildFreezeRequest(sender, args, sppPlayer.getPlayer()));
        return true;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase(ENABLED) || args[0].equalsIgnoreCase(DISABLED)) {
            return Optional.ofNullable(args[1]);
        }
        return Optional.of(args[0]);
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        if(args.length > 0) {
            if (args[0].equalsIgnoreCase(ENABLED) || args[0].equalsIgnoreCase(DISABLED)) {
                return 2;
            }
        }
        return 1;
    }

    @Override
    protected boolean isDelayable() {
        return true;
    }

    @Override
    protected boolean isAuthenticationRequired() {
        return true;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return ONLINE;
    }

    @Override
    protected boolean canBypass(Player player) {
        return permission.has(player, options.permissionFreezeBypass);
    }

    private FreezeRequest buildFreezeRequest(CommandSender sender, String[] args, Player targetPlayer) {
        boolean freeze = !freezeHandler.isFrozen(targetPlayer.getUniqueId());

        if (args[0].equalsIgnoreCase(ENABLED) || args[0].equalsIgnoreCase(DISABLED)) {
            freeze = args[0].equalsIgnoreCase(ENABLED);
        }

        return new FreezeRequest(sender, targetPlayer, freeze);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> onlinePlayers = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        List<String> offlinePlayers = Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).collect(Collectors.toList());

        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            if (!args[0].equalsIgnoreCase(ENABLED) && !args[0].equalsIgnoreCase(DISABLED)) {
                    suggestions.add(ENABLED);
                    suggestions.add(DISABLED);
            }
            suggestions.addAll(onlinePlayers);
            suggestions.addAll(offlinePlayers);
            return suggestions.stream()
                    .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                    .collect(Collectors.toList());
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase(ENABLED) || args[0].equalsIgnoreCase(DISABLED)) {
                suggestions.addAll(onlinePlayers);
                suggestions.addAll(offlinePlayers);
                return suggestions.stream()
                        .filter(s -> args[1].isEmpty() || s.contains(args[1]))
                        .collect(Collectors.toList());
            }
        }

        suggestions.addAll(argumentProcessor.getArgumentsSuggestions(sender, args[args.length - 1], VALID_ARGUMENTS));
        suggestions.add(DELAY.getPrefix());
        return suggestions;
    }
}