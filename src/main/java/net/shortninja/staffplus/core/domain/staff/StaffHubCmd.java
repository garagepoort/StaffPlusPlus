package net.shortninja.staffplus.core.domain.staff;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.tubinggui.GuiActionService;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.NONE;

@Command(
    command = "commands:staff-hub",
    permissions = "permissions:staff-hub.view",
    description = "Open the Staff Hub GUI.",
    usage = "[]",
    playerRetrievalStrategy = NONE
)
@IocBean(conditionalOnProperty = "mute-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class StaffHubCmd extends AbstractCmd {

    private final GuiActionService guiActionService;

    public StaffHubCmd(Messages messages,
                       CommandService commandService,
                       PermissionHandler permissionHandler, GuiActionService guiActionService) {
        super(messages, permissionHandler, commandService);
        this.guiActionService = guiActionService;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        if (!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }

        guiActionService.executeAction((Player) sender, "hub/view");
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 0;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }
}
