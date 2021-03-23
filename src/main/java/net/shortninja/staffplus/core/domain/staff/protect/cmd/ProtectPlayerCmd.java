package net.shortninja.staffplus.core.domain.staff.protect.cmd;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class ProtectPlayerCmd extends AbstractCmd {

    private final SessionManagerImpl sessionManager = StaffPlus.get().iocContainer.get(SessionManagerImpl.class);
    private final MessageCoordinator message = StaffPlus.get().iocContainer.get(MessageCoordinator.class);

    public ProtectPlayerCmd(String name) {
        super(name, StaffPlus.get().iocContainer.get(Options.class).protectConfiguration.getPermissionProtectPlayer());
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
