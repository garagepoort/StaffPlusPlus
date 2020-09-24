package net.shortninja.staffplus.server.command.cmd.mode;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.BusinessException;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class ModeCmd extends AbstractCmd {
    public static final String ENABLE = "enable";
    public static final String DISABLE = "disable";

    public ModeCmd(String name) {
        super(name, IocContainer.getOptions().permissionMode);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer) {
        if (args.length >= 2 && permission.isOp(sender)) {
            String option = args[1];

            if (option.equalsIgnoreCase(ENABLE)) {
                StaffPlus.get().modeCoordinator.addMode(targetPlayer.getPlayer());
            } else if (option.equalsIgnoreCase(DISABLE)) {
                StaffPlus.get().modeCoordinator.removeMode(targetPlayer.getPlayer());
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
    protected boolean canBypass(Player player) {
        return false;
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
    protected boolean isAuthenticationRequired() {
        return true;
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

    @Override
    protected boolean isDelayable() {
        return false;
    }

    private void toggleMode(Player player) {
        if (StaffPlus.get().modeCoordinator.isInMode(player.getUniqueId())) {
            StaffPlus.get().modeCoordinator.removeMode(player);
        } else StaffPlus.get().modeCoordinator.addMode(player);
    }
}