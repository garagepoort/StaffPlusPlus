package net.shortninja.staffplus.core.domain.staff.mode.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.NONE;

@IocBean
@IocMultiProvider(SppCommand.class)
public class StaffFlyCmd extends AbstractCmd {
    private final StaffModeService staffModeService;

    public StaffFlyCmd(Messages messages, Options options, CommandService commandService, StaffModeService staffModeService) {
        super(options.commandStaffFly, messages, options, commandService);
        this.staffModeService = staffModeService;
        setPermission(options.permissionMode);
        setDescription("Toggles fly while in staff mode");
    }

    @Override
    public boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer) {
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
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return NONE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }
}