package net.shortninja.staffplus.core.domain.staff.location.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.location.StaffLocationService;
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

    private final StaffLocationService staffLocationService;

    public CreateStaffLocationCmd(Messages messages, PermissionHandler permissionHandler, CommandService commandService, StaffLocationService staffLocationService) {
        super(messages, permissionHandler, commandService);
        this.staffLocationService = staffLocationService;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        Player initiator = validateIsPlayer(sender);
        staffLocationService.saveLocation(initiator, JavaUtils.compileWords(args, 0));
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
