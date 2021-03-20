package net.shortninja.staffplus.domain.staff.protect.cmd;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.domain.player.SppPlayer;
import net.shortninja.staffplus.common.cmd.AbstractCmd;
import net.shortninja.staffplus.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManagerImpl;
import net.shortninja.staffplus.common.utils.MessageCoordinator;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class ProtectPlayerCmd extends AbstractCmd {

    private final SessionManagerImpl sessionManager = IocContainer.getSessionManager();
    private final MessageCoordinator message = IocContainer.getMessage();

    public ProtectPlayerCmd(String name) {
        super(name, IocContainer.getOptions().protectConfiguration.getPermissionProtectPlayer());
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        PlayerSession playerSession = sessionManager.get(player.getId());
        playerSession.setProtected(!playerSession.isProtected());

        if (playerSession.isProtected()) {
            message.send(sender, player.getUsername() + " is now protected from all damage", messages.prefixGeneral);
            message.send(player.getPlayer(), "You have been protected from all damage by a staff member", messages.prefixGeneral);
        } else {
            message.send(sender, player.getUsername() + " is no longer protected from all damage", messages.prefixGeneral);
            message.send(player.getPlayer(), "You are no longer protected from damage", messages.prefixGeneral);
        }
        return true;
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
    protected boolean isDelayable() {
        return true;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.of(args[0]);
    }
}
