package net.shortninja.staffplus.staff.protect.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class ProtectPlayerCmd extends AbstractCmd {

    private final SessionManager sessionManager = IocContainer.getSessionManager();
    private final MessageCoordinator message = IocContainer.getMessage();

    public ProtectPlayerCmd(String name) {
        super(name, IocContainer.getOptions().permissionProtectPlayer);
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
