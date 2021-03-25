package net.shortninja.staffplus.core.domain.staff.mode.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentExecutor;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType;
import net.shortninja.staffplus.core.domain.player.StripService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

@IocBean
@IocMultiProvider(ArgumentExecutor.class)
public class StripArgumentExecutor implements ArgumentExecutor {

    private final StripService stripService;

    public StripArgumentExecutor(StripService stripService) {
        this.stripService = stripService;
    }

    @Override
    public boolean execute(CommandSender commandSender, String playerName, String value) {
        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) {
            return false;
        }

        stripService.strip(commandSender, player);
        return true;
    }

    @Override
    public ArgumentType getType() {
        return ArgumentType.STRIP;
    }

    @Override
    public List<String> complete(CommandSender sender, String currentArg) {
        return Collections.singletonList(getType().getPrefix());
    }
}
