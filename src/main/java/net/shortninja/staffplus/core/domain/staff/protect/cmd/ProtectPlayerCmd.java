package net.shortninja.staffplus.core.domain.staff.protect.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;

import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import org.bukkit.command.CommandSender;

import java.util.Optional;

@IocBean(conditionalOnProperty = "protect-module.player-enabled=true")
@IocMultiProvider(SppCommand.class)
public class ProtectPlayerCmd extends AbstractCmd {

    private final SessionManagerImpl sessionManager;

    public ProtectPlayerCmd(Messages messages, Options options, SessionManagerImpl sessionManager, CommandService commandService) {
        super(options.protectConfiguration.getCommandProtectPlayer(), messages, options, commandService);
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
