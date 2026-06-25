package net.shortninja.staffplus.core.domain.staff.protect.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.Optional;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.ONLINE;

@Command(
    command = "commands:protect-player",
    permissions = "permissions:protect-player",
    description = "Protect a player from all damage",
    usage = "[player]",
    delayable = true,
    playerRetrievalStrategy = ONLINE
)
@IocBean(conditionalOnProperty = "protect-module.player-enabled=true")
@IocMultiProvider(SppCommand.class)
public class ProtectPlayerCmd extends AbstractCmd {

    private final OnlineSessionsManager sessionManager;

    public ProtectPlayerCmd(Messages messages, OnlineSessionsManager sessionManager, CommandService commandService, PermissionHandler permissionHandler) {
        super(messages, permissionHandler, commandService);
        this.sessionManager = sessionManager;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        OnlinePlayerSession playerSession = sessionManager.get(player.getPlayer());
        playerSession.setProtected(!playerSession.isProtected());

        if (playerSession.isProtected()) {
            messages.sendTranslation(sender, "protect-player-enabled-staff", messages.prefixGeneral, "%player%", player.getUsername());
            messages.sendTranslation(player.getPlayer(), "protect-player-enabled-target", messages.prefixGeneral);
        } else {
            messages.sendTranslation(sender, "protect-player-disabled-staff", messages.prefixGeneral, "%player%", player.getUsername());
            messages.sendTranslation(player.getPlayer(), "protect-player-disabled-target", messages.prefixGeneral);
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
