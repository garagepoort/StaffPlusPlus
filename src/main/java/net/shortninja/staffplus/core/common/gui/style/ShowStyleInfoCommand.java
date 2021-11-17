package net.shortninja.staffplus.core.common.gui.style;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;


@Command(
    command = "commands:show-style-info",
    permissions = "permissions:show-style-info",
    description = "Toggle seeing style ids inside the Staff++ guis"
)
@IocBean
@IocMultiProvider(SppCommand.class)
public class ShowStyleInfoCommand extends AbstractCmd {

    private final OnlineSessionsManager onlineSessionsManager;

    public ShowStyleInfoCommand(Messages messages, PermissionHandler permissionHandler, CommandService commandService, OnlineSessionsManager onlineSessionsManager) {
        super(messages, permissionHandler, commandService);
        this.onlineSessionsManager = onlineSessionsManager;
    }

    @Override
    public boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        validateIsPlayer(sender);
        PlayerSession playerSession = onlineSessionsManager.get((Player) sender);
        playerSession.setCanViewStyleIds(!playerSession.isCanViewStyleIds());
        messages.send(sender, "Style info " + (playerSession.isCanViewStyleIds() ? "enabled" : "disabled"), messages.prefixGeneral);
        return true;
    }

    @Override
    public int getMinimumArguments(CommandSender sender, String[] args) {
        return 0;
    }

    @Override
    public Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }
}
