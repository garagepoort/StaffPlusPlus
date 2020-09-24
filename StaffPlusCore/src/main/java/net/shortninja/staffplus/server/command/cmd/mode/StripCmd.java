package net.shortninja.staffplus.server.command.cmd.mode;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.player.StripService;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

import static net.shortninja.staffplus.server.command.PlayerRetrievalStrategy.ONLINE;

public class StripCmd extends AbstractCmd {
    private final MessageCoordinator message = IocContainer.getMessage();
    private final StripService stripService = StripService.getInstance();

    public StripCmd(String name) {
        super(name, IocContainer.getOptions().permissionStrip);
    }

    @Override
    public boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer) {
        if (JavaUtils.hasInventorySpace(targetPlayer.getPlayer())) {
            stripService.strip(sender, targetPlayer.getPlayer());
        } else {
            message.send(sender, messages.invalidArguments.replace("%usage%", usageMessage), messages.prefixGeneral);
        }

        return true;
    }

    @Override
    protected boolean canBypass(Player player) {
        return false;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            return 0;
        }
        return 1;
    }

    @Override
    protected boolean isAuthenticationRequired() {
        return true;
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

    @Override
    protected boolean isDelayable() {
        return true;
    }
}