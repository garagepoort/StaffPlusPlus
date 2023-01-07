package net.shortninja.staffplus.core.domain.staff.nightvision.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.nightvision.NightVisionService;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Command(
    command = "commands:nightvision",
    permissions = "permissions:nightvision",
    description = "Toggle nightvision",
    playerRetrievalStrategy = PlayerRetrievalStrategy.OPTIONAL_ONLINE
)
@IocBean
@IocMultiProvider(SppCommand.class)
public class NightvisionCmd extends AbstractCmd {

    private final NightVisionService nightVisionService;

    public NightvisionCmd(Messages messages, CommandService commandService, PermissionHandler permissionHandler, NightVisionService nightVisionService) {
        super(messages, permissionHandler, commandService);
        this.nightVisionService = nightVisionService;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        String option = args[0];

        if(!(sender instanceof Player)) {
            toggleNightVision(player.getPlayer(), option);
        } else {
            toggleNightVision((Player) sender, option);
        }
        return true;
    }

    private void toggleNightVision(Player sender, String option) {
        if("on".equalsIgnoreCase(option)) {
            nightVisionService.turnOnNightVision("COMMAND", sender);
        }else {
            nightVisionService.turnOffNightVision("COMMAND", sender);
        }
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 1;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            return Optional.ofNullable(args[1]);
        }
        return Optional.empty();
    }

    @Override
    protected List<String> autoComplete(CommandSender sender, String[] args, String[] sppArgs) throws IllegalArgumentException {
        String currentArg = args.length > 0 ? args[args.length - 1] : "";

        if (args.length == 1) {
            return Arrays.asList("on", "off").stream()
                .filter(s -> currentArg.isEmpty() || s.contains(currentArg))
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}

