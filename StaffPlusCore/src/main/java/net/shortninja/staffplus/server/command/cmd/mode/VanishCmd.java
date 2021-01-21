package net.shortninja.staffplus.server.command.cmd.mode;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.staff.vanish.VanishHandler;
import net.shortninja.staffplus.unordered.VanishType;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

import static net.shortninja.staffplus.server.command.PlayerRetrievalStrategy.ONLINE;

public class VanishCmd extends AbstractCmd {
    private final PermissionHandler permission = IocContainer.getPermissionHandler();
    private final MessageCoordinator message = IocContainer.getMessage();
    private final Options options = IocContainer.getOptions();
    private final Messages messages = IocContainer.getMessages();
    private final SessionManager sessionManager = IocContainer.getSessionManager();
    private final VanishHandler vanishHandler = IocContainer.getVanishHandler();

    public VanishCmd(String name) {
        super(name, IocContainer.getOptions().permissionVanishCommand);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer) {
        if (args.length >= 3 && permission.isOp(sender)) {
            String option = args[2];

            if (option.equalsIgnoreCase("enable")) {
                handleVanishArgument(sender, args[0], targetPlayer.getPlayer(), false);
            } else {
                vanishHandler.removeVanish(targetPlayer.getPlayer());
            }
            return true;
        }

        if (args.length == 2 && permission.isOp(sender)) {
            handleVanishArgument(sender, args[0], targetPlayer.getPlayer(), false);
            return true;
        }

        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                throw new BusinessException(messages.onlyPlayers);
            }
            handleVanishArgument(sender, args[0], (Player) sender, true);
            return true;
        }

        sendHelp(sender);
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            return 1;
        }
        return 2;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return ONLINE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        if (args.length > 1) {
            return Optional.of(args[1]);
        }
        if (sender instanceof Player) {
            return Optional.of(sender.getName());
        }
        return Optional.empty();
    }

    private void handleVanishArgument(CommandSender sender, String argument, Player player, boolean shouldCheckPermission) {
        boolean isValid = JavaUtils.isValidEnum(VanishType.class, argument.toUpperCase());
        VanishType vanishType = VanishType.NONE;
        PlayerSession user = sessionManager.get(player.getUniqueId());

        if (!isValid) {
            sendHelp(sender);
            return;
        } else vanishType = VanishType.valueOf(argument.toUpperCase());

        switch (vanishType) {
            case TOTAL:
                if (permission.has(player, options.permissionVanishTotal) || !shouldCheckPermission) {
                    if (user.getVanishType() != VanishType.TOTAL) {
                        vanishHandler.addVanish(player, vanishType);
                    } else vanishHandler.removeVanish(player);
                } else message.send(player, messages.noPermission, messages.prefixGeneral);
                break;
            case LIST:
                if (permission.has(player, options.permissionVanishList) || !shouldCheckPermission) {
                    if (user.getVanishType() != VanishType.LIST) {
                        vanishHandler.addVanish(player, vanishType);
                    } else vanishHandler.removeVanish(player);
                } else message.send(player, messages.noPermission, messages.prefixGeneral);
                break;
            case NONE:
                if (permission.has(player, options.permissionVanishList) || permission.has(player, options.permissionVanishTotal) || !shouldCheckPermission) {
                    vanishHandler.removeVanish(player);
                } else message.send(player, messages.noPermission, messages.prefixGeneral);
                break;
        }
    }

    private void sendHelp(CommandSender sender) {
        message.send(sender, "&7" + message.LONG_LINE, "");
        message.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixGeneral);
        message.send(sender, "&7" + message.LONG_LINE, "");
    }
}