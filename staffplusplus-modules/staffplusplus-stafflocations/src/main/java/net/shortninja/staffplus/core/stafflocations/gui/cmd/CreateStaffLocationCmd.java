package net.shortninja.staffplus.core.stafflocations.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.tubinggui.GuiActionBuilder;
import be.garagepoort.mcioc.tubinggui.GuiActionService;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.JavaUtils;
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
    command = "commands:staff-locations.create",
    permissions = "permissions:staff-locations.create",
    description = "Create a staff location.",
    usage = "[name]"
)
@IocBean(conditionalOnProperty = "staff-locations-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class CreateStaffLocationCmd extends AbstractCmd {

    private final GuiActionService guiActionService;

    public CreateStaffLocationCmd(Messages messages, PermissionHandler permissionHandler, CommandService commandService, GuiActionService guiActionService) {
        super(messages, permissionHandler, commandService);
        this.guiActionService = guiActionService;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        Player initiator = validateIsPlayer(sender);
        String locationName = JavaUtils.compileWords(args, 0);
        guiActionService.executeAction(initiator, GuiActionBuilder.fromAction("staff-locations/create-flow/select-icon")
            .param("locationName", locationName)
            .build());
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 1;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }
}
