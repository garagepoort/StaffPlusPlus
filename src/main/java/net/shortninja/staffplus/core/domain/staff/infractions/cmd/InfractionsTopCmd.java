package net.shortninja.staffplus.core.domain.staff.infractions.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.GuiActionService;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionType;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Command(
    command = "commands:infractions-top-view",
    permissions = "permissions:infractions.view",
    description = "View the top list of players with the most infractions",
    usage = "[infractionType?]"
)
@IocBean(conditionalOnProperty = "infractions-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class InfractionsTopCmd extends AbstractCmd {

    private final GuiActionService guiActionService;

    public InfractionsTopCmd(Messages messages, CommandService commandService, PermissionHandler permissionHandler, GuiActionService guiActionService) {
        super(messages, permissionHandler, commandService);
        this.guiActionService = guiActionService;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        if (!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }

        Player p = (Player) sender;
        if (args.length == 1) {
            if (!JavaUtils.isValidEnum(InfractionType.class, args[0])) {
                throw new BusinessException("&CInvalid infraction type provided");
            }

            guiActionService.executeAction(p, GuiActionBuilder.builder()
                .action("manage-infractions/view/top")
                .param("infractionType", args[0])
                .build());
        } else {
            guiActionService.executeAction(p, GuiActionBuilder.builder()
                .action("manage-infractions/view/top")
                .build());
        }

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

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return Arrays.stream(InfractionType.values()).map(Enum::name).collect(Collectors.toList());
    }
}
