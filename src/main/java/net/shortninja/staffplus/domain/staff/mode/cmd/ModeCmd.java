package net.shortninja.staffplus.domain.staff.mode.cmd;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.cmd.AbstractCmd;
import net.shortninja.staffplus.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.domain.player.SppPlayer;
import net.shortninja.staffplus.domain.staff.mode.StaffModeService;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionLoader;
import net.shortninja.staffplus.session.SessionManagerImpl;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class ModeCmd extends AbstractCmd {
    private static final String ENABLE = "enable";
    private static final String DISABLE = "disable";
    private final StaffModeService staffModeService = IocContainer.getModeCoordinator();
    private final SessionManagerImpl sessionManager = IocContainer.getSessionManager();
    private final SessionLoader sessionLoader = IocContainer.getSessionLoader();

    public ModeCmd(String name) {
        super(name, IocContainer.getOptions().permissionMode);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer) {
        if (!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }

        if (args.length >= 2 && permission.isOp(sender)) {
            String option = args[1];

            if (option.equalsIgnoreCase(ENABLE)) {
                staffModeService.addMode(targetPlayer.getPlayer());
                sessionLoader.saveSession(sessionManager.get(targetPlayer.getId()));
            } else if (option.equalsIgnoreCase(DISABLE)) {
                staffModeService.removeMode(targetPlayer.getPlayer());
                sessionLoader.saveSession(sessionManager.get(targetPlayer.getId()));
            } else {
                throw new BusinessException(messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()), messages.prefixGeneral);
            }

        } else if (args.length == 1 && permission.isOp(sender)) {
            toggleMode(targetPlayer.getPlayer());
            sessionLoader.saveSession(sessionManager.get(targetPlayer.getId()));
        } else {
            toggleMode((Player) sender);
            sessionLoader.saveSession(sessionManager.get(((Player) sender).getUniqueId()));
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
        PlayerSession session = sessionManager.get(player.getUniqueId());
        if (session.isInStaffMode()) {
            staffModeService.removeMode(player);
        } else {
            staffModeService.addMode(player);
        }
    }
}