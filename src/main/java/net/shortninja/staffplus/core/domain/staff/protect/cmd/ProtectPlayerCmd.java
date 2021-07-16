package net.shortninja.staffplus.core.domain.staff.protect.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.common.cmd.*;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.Optional;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.ONLINE;

@Command(
    command = "commands:commands.protect-player",
    permissions = "permissions:permissions.protect-player",
    description = "Protect a player from all damage",
    usage = "[player]",
    delayable = true,
    playerRetrievalStrategy = ONLINE
)
@IocBean(conditionalOnProperty = "protect-module.player-enabled=true")
@IocMultiProvider(SppCommand.class)
public class ProtectPlayerCmd extends AbstractCmd {

    private final SessionManagerImpl sessionManager;

    public ProtectPlayerCmd(Messages messages, SessionManagerImpl sessionManager, CommandService commandService, PermissionHandler permissionHandler) {
        super(messages, permissionHandler, commandService);
        this.sessionManager = sessionManager;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        PlayerSession playerSession = sessionManager.get(player.getId());
        playerSession.setProtected(!playerSession.isProtected());

        if (playerSession.isProtected()) {
            messages.send(sender, player.getUsername() + " is now protected from all damage", messages.prefixGeneral);
            messages.send(player.getPlayer(), "You have been protected from all damage by a staff member", messages.prefixGeneral);
        } else {
            messages.send(sender, player.getUsername() + " is no longer protected from all damage", messages.prefixGeneral);
            messages.send(player.getPlayer(), "You are no longer protected from damage", messages.prefixGeneral);
        }
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 1;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.of(args[0]);
    }
}
