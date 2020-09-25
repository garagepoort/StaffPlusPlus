package net.shortninja.staffplus.server.command.cmd.mode;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.BusinessException;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.player.StripService;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

import static net.shortninja.staffplus.server.command.PlayerRetrievalStrategy.ONLINE;

public class StripCmd extends AbstractCmd {
    private final StripService stripService = StripService.getInstance();

    public StripCmd(String name) {
        super(name, IocContainer.getOptions().permissionStrip);
    }

    @Override
    public boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer) {
        stripService.strip(sender, targetPlayer.getPlayer());
        return true;
    }

    @Override
    protected void validateExecution(SppPlayer player) {
        if (!JavaUtils.hasInventorySpace(player.getPlayer())) {
            throw new BusinessException("Cannot strip this player. His inventory has no more space left");
        }
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            return 0;
        }
        return 1;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return ONLINE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        if (args.length == 0 && (sender instanceof Player)) {
            return Optional.of(sender.getName());
        }
        return Optional.of(args[0]);
    }
}