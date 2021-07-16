package net.shortninja.staffplus.core.domain.staff.mode.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.cmd.*;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;

import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.mode.handler.GadgetHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

@Command(
    command = "commands:commands.follow",
    permissions = "permissions:permissions.follow",
    description = "Follows or unfollows the player.",
    usage = "[player]",
    playerRetrievalStrategy = PlayerRetrievalStrategy.ONLINE
)
@IocBean
@IocMultiProvider(SppCommand.class)
public class FollowCmd extends AbstractCmd {

    private final GadgetHandler gadgetHandler;

    public FollowCmd(Messages messages,
                     Options options,
                     GadgetHandler gadgetHandler,
                     CommandService commandService,
                     PermissionHandler permissionHandler) {
        super(messages, permissionHandler, commandService);
        this.gadgetHandler = gadgetHandler;
    }

    @Override
    public boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer, Map<String, String> optionalParameters) {
        if (!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }

        gadgetHandler.onFollow((Player) sender, targetPlayer.getPlayer());
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

}