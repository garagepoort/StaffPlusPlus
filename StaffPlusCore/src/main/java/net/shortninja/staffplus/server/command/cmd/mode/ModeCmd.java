package net.shortninja.staffplus.server.command.cmd.mode;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.staff.mode.ModeCoordinator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class ModeCmd extends AbstractCmd {
    private static final String ENABLE = "enable";
    private static final String DISABLE = "disable";
    private final ModeCoordinator modeCoordinator = IocContainer.getModeCoordinator();

    public ModeCmd(String name) {
        super(name, IocContainer.getOptions().permissionMode);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer) {
        if (args.length >= 2 && permission.isOp(sender)) {
            String option = args[1];

            if (option.equalsIgnoreCase(ENABLE)) {
                modeCoordinator.addMode(targetPlayer.getPlayer());
            } else if (option.equalsIgnoreCase(DISABLE)) {
                modeCoordinator.removeMode(targetPlayer.getPlayer());
            } else {
                throw new BusinessException(messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()), messages.prefixGeneral);
            }

        } else if (args.length == 1 && permission.isOp(sender)) {
            toggleMode(targetPlayer.getPlayer());
        } else if (sender instanceof Player) {
            toggleMode((Player) sender);
        } else {
            throw new BusinessException(messages.onlyPlayers);
        }

        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            if (args[1].equalsIgnoreCase(ENABLE) || args[1].equalsIgnoreCase(DISABLE)) {
                return 2;
            }
        }
        if (args.length == 1) {
            return 1;
        }
        if (sender instanceof Player) {
            return 0;
        }
        return 1;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.ONLINE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        if (args.length == 0 && (sender instanceof Player)) {
            return Optional.of(sender.getName());
        }
        return Optional.of(args[0]);
    }

    private void toggleMode(Player player) {
        if (modeCoordinator.isInMode(player.getUniqueId())) {
            modeCoordinator.removeMode(player);
        } else modeCoordinator.addMode(player);
    }
}