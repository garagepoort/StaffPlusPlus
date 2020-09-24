package net.shortninja.staffplus.server.command.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.player.attribute.mode.handler.ReviveHandler;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.server.command.arguments.ArgumentType;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.Bukkit;
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

public class ReviveCmd extends AbstractCmd {
    private static final List<ArgumentType> VALID_ARGUMENTS = Arrays.asList(TELEPORT, STRIP, HEALTH);

    private final MessageCoordinator message = IocContainer.getMessage();
    private final ReviveHandler reviveHandler = StaffPlus.get().reviveHandler;

    public ReviveCmd(String name) {
        super(name, IocContainer.getOptions().permissionRevive);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if (reviveHandler.hasSavedInventory(player.getPlayer().getUniqueId())) {
            reviveHandler.restoreInventory(player.getPlayer());
            argumentProcessor.parseArguments(sender, player.getPlayer().getName(), Arrays.asList(args), VALID_ARGUMENTS);
            message.send(sender, messages.revivedStaff.replace("%target%", player.getPlayer().getName()), messages.prefixGeneral);
        } else {
            message.send(sender, messages.noFound, messages.prefixGeneral);
        }

        return true;
    }

    @Override
    protected boolean canBypass(Player player) {
        return false;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            return 0;
        }
        return 1;
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
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        if (args.length == 0 && (sender instanceof Player)) {
            return Optional.of(sender.getName());
        }
        return Optional.of(args[0]);
    }

    @Override
    protected boolean isDelayable() {
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> onlinePlayers = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            suggestions.addAll(onlinePlayers);
            return suggestions;
        }

        suggestions.addAll(argumentProcessor.getArgumentsSuggestions(sender, args[args.length - 1], VALID_ARGUMENTS));
        return suggestions;
    }
}