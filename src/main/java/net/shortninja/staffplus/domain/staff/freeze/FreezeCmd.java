package net.shortninja.staffplus.domain.staff.freeze;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.domain.player.PlayerManager;
import net.shortninja.staffplus.domain.player.SppPlayer;
import net.shortninja.staffplus.common.cmd.AbstractCmd;
import net.shortninja.staffplus.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.common.cmd.arguments.ArgumentType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.common.cmd.PlayerRetrievalStrategy.ONLINE;
import static net.shortninja.staffplus.common.cmd.arguments.ArgumentType.*;

public class FreezeCmd extends AbstractCmd {
    private static final String ENABLED = "enabled";
    private static final String DISABLED = "disabled";

    private final FreezeHandler freezeHandler = IocContainer.getFreezeHandler();
    private final PlayerManager playerManager = IocContainer.getPlayerManager();

    public FreezeCmd(String name) {
        super(name, IocContainer.getOptions().permissionFreeze);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer sppPlayer) {
        freezeHandler.execute(buildFreezeRequest(sender, args, sppPlayer.getPlayer()));
        return true;
    }

    @Override
    protected List<ArgumentType> getPreExecutionSppArguments() {
        return Arrays.asList(TELEPORT, STRIP, HEALTH);
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
        if (args.length > 0) {
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

        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            if (!args[0].equalsIgnoreCase(ENABLED) && !args[0].equalsIgnoreCase(DISABLED)) {
                suggestions.add(ENABLED);
                suggestions.add(DISABLED);
            }
            suggestions.addAll(playerManager.getAllPlayerNames());
            return suggestions.stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase(ENABLED) || args[0].equalsIgnoreCase(DISABLED)) {
                return playerManager.getAllPlayerNames().stream()
                    .filter(s -> args[1].isEmpty() || s.contains(args[1]))
                    .collect(Collectors.toList());
            }
        }

        return getSppArgumentsSuggestions(sender, args);
    }
}