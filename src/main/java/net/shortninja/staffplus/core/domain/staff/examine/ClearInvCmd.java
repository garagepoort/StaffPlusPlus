package net.shortninja.staffplus.core.domain.staff.examine;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.*;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;

import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.ONLINE;
import static net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType.HEALTH;
import static net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType.TELEPORT;

@IocBean
@IocMultiProvider(SppCommand.class)
@Command(
    command = "commands:commands.clearInv",
    permissions = "permissions:permissions.invClear",
    description = "Used to clear a desired player's inventory",
    usage = "[player]",
    delayable = true
)
public class ClearInvCmd extends AbstractCmd {

    private final PermissionHandler permissionHandler;
    private final PlayerManager playerManager;

    public ClearInvCmd(PermissionHandler permissionHandler, Messages messages, Options options, CommandService commandService, PlayerManager playerManager) {
        super(messages, options, commandService);
        this.permissionHandler = permissionHandler;
        this.playerManager = playerManager;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer, Map<String, String> optionalParameters) {
        JavaUtils.clearInventory(targetPlayer.getPlayer());
        sender.sendMessage(targetPlayer.getPlayer().getName() + "'s inventory has been cleared");
        return true;
    }

    @Override
    protected List<ArgumentType> getPostExecutionSppArguments() {
        return Arrays.asList(TELEPORT, HEALTH);
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
        return ONLINE;
    }

    @Override
    protected boolean canBypass(Player player) {
        return permissionHandler.has(player, options.permissionClearInvBypass);
    }

    @Override
    public List<String> autoComplete(CommandSender sender, String[] args, String[] sppArgs) throws IllegalArgumentException {
        if (args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                    .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
