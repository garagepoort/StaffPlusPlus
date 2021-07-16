package net.shortninja.staffplus.core.domain.staff.mode.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

@Command(
    command = "commands:staff-mode-fly",
    permissions = "permissions:mode",
    description = "Toggles fly while in staff mode"
)
@IocBean
@IocMultiProvider(SppCommand.class)
public class StaffFlyCmd extends AbstractCmd {
    private final StaffModeService staffModeService;

    public StaffFlyCmd(Messages messages, CommandService commandService, StaffModeService staffModeService, PermissionHandler permissionHandler) {
        super(messages, permissionHandler, commandService);
        this.staffModeService = staffModeService;
    }

    @Override
    public boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer, Map<String, String> optionalParameters) {
        if (!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }
        Player player = (Player) sender;
        staffModeService.toggleStaffFly(player);
        String onOff = player.getAllowFlight() ? "on" : "off";
        messages.send(player, "&3Flight is now &6" + onOff, messages.prefixGeneral);
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