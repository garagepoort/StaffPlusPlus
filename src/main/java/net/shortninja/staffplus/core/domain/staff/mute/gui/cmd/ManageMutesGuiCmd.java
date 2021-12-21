package net.shortninja.staffplus.core.domain.staff.mute.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.GuiActionService;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.OPTIONAL_BOTH;

@Command(
    command = "commands:mutes.manage.gui",
    permissions = "permissions:mute-view",
    description = "Open the manage Mutes GUI.",
    usage = "[player]",
    playerRetrievalStrategy = OPTIONAL_BOTH
)
@IocBean(conditionalOnProperty = "mute-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class ManageMutesGuiCmd extends AbstractCmd {

    private final GuiActionService guiActionService;
    private final PlayerManager playerManager;

    public ManageMutesGuiCmd(Messages messages,
                             CommandService commandService,
                             PermissionHandler permissionHandler, GuiActionService guiActionService, PlayerManager playerManager) {
        super(messages, permissionHandler, commandService);
        this.guiActionService = guiActionService;
        this.playerManager = playerManager;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        if (!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }

        guiActionService.executeAction((Player) sender, GuiActionBuilder.builder()
            .action("manage-mutes/view/all-active")
            .param("targetPlayerName", player == null ? null : player.getUsername())
            .build());
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 0;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(args[0]);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
