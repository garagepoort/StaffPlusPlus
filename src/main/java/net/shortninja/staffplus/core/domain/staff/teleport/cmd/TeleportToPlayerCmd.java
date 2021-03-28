package net.shortninja.staffplus.core.domain.staff.teleport.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.teleport.TeleportService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType.HEALTH;
import static net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType.STRIP;

@IocBean
@IocMultiProvider(SppCommand.class)
public class TeleportToPlayerCmd extends AbstractCmd {

    private final TeleportService teleportService;

    public TeleportToPlayerCmd(Messages messages, MessageCoordinator message, Options options, TeleportService teleportService, CommandService commandService) {
        super(options.commandTeleportToPlayer, messages, message, options, commandService);
        this.teleportService = teleportService;
        setDescription("Teleport yourself to a specific player");
        setUsage("{player}");
        setPermission(options.permissionTeleportToPlayer);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer) {
        if (!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }

        teleportService.teleportToPlayer((Player) sender, targetPlayer.getPlayer());
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
        return 1;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.ONLINE;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            List<String> onlinePlayers = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
            return onlinePlayers.stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }

        return getSppArgumentsSuggestions(sender, args);
    }
}