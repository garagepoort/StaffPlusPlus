package net.shortninja.staffplus.core.domain.staff.protect.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.authentication.AuthenticationService;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentProcessor;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.delayedactions.DelayArgumentExecutor;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.command.CommandSender;

import java.util.Optional;

@IocBean(conditionalOnProperty = "protect-module.player-enabled=true")
@IocMultiProvider(SppCommand.class)
public class ProtectPlayerCmd extends AbstractCmd {

    private final SessionManagerImpl sessionManager;

    public ProtectPlayerCmd(PermissionHandler permissionHandler, AuthenticationService authenticationService, Messages messages, MessageCoordinator message, PlayerManager playerManager, Options options, DelayArgumentExecutor delayArgumentExecutor, ArgumentProcessor argumentProcessor, SessionManagerImpl sessionManager) {
        super(options.protectConfiguration.getCommandProtectPlayer(), permissionHandler, authenticationService, messages, message, playerManager, options, delayArgumentExecutor, argumentProcessor);
        this.sessionManager = sessionManager;
        setPermission(options.protectConfiguration.getPermissionProtectPlayer());
        setDescription("Protect a player from all damage");
        setUsage("[player]");
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
