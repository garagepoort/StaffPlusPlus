package net.shortninja.staffplus.core.domain.player.ip.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.ip.PlayerIpService;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Command(
    command = "commands:ips.clear-ips",
    permissions = "permissions:ips.clear-ips",
    description = "Clears all ip history of a player",
    usage = "[player]",
    playerRetrievalStrategy = PlayerRetrievalStrategy.BOTH
)
@IocBean
@IocMultiProvider(SppCommand.class)
public class ClearIpsCommand extends AbstractCmd {

    private final PlayerIpService playerIpService;
    private final PlayerManager playerManager;

    public ClearIpsCommand(Messages messages, PermissionHandler permissionHandler, CommandService commandService, PlayerIpService playerIpService, PlayerManager playerManager) {
        super(messages, permissionHandler, commandService);
        this.playerIpService = playerIpService;
        this.playerManager = playerManager;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> playerIpService.clearHistory(sender, player));
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 1;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.ofNullable(args[0]);
    }

    @Override
    public List<String> autoComplete(CommandSender sender, String[] args, String[] sppArgs) throws IllegalArgumentException {
        String currentArg = args.length > 0 ? args[args.length - 1] : "";

        if (args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> currentArg.isEmpty() || s.contains(currentArg))
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
