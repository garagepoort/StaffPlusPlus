package net.shortninja.staffplus.server.command;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.BusinessException;
import net.shortninja.staffplus.common.NoPermissionException;
import net.shortninja.staffplus.common.PlayerOfflineException;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.arguments.ArgumentProcessor;
import net.shortninja.staffplus.server.command.arguments.DelayArgumentExecutor;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.shortninja.staffplus.server.command.PlayerRetrievalStrategy.NONE;

public abstract class AbstractCmd extends BukkitCommand {
    private final DelayArgumentExecutor delayArgumentExecutor = new DelayArgumentExecutor();
    protected final PermissionHandler permission = IocContainer.getPermissionHandler();
    protected final Messages messages = IocContainer.getMessages();
    protected final PlayerManager playerManager = IocContainer.getPlayerManager();
    protected final ArgumentProcessor argumentProcessor = ArgumentProcessor.getInstance();
    protected final Options options = IocContainer.getOptions();

    protected AbstractCmd(String name, String permission) {
        super(name);
        setPermission(permission);
    }

    protected AbstractCmd(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        try {
            validateAuthentication(sender);
            validatePermissions(sender);
            validateMinimumArguments(sender, args);

            SppPlayer player = retrievePlayer(sender, args);
            if (player != null) {
                if (player.isOnline() && canBypass(player.getPlayer())) {
                    throw new BusinessException(messages.bypassed, messages.prefixGeneral);
                }
                if (shouldDelay(args)) {
                    delayCommand(sender, alias, args, player.getUsername());
                    return true;
                }
            }

            return executeCmd(sender, alias, args, player);
        } catch (BusinessException e) {
            IocContainer.getMessage().send(sender, e.getMessage(), e.getPrefix());
            return false;
        }
    }

    protected abstract boolean canBypass(Player player);

    protected abstract boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player);

    protected abstract int getMinimumArguments(CommandSender sender, String[] args);

    protected abstract boolean isAuthenticationRequired();

    protected abstract PlayerRetrievalStrategy getPlayerRetrievalStrategy();

    protected abstract Optional<String> getPlayerName(CommandSender sender, String[] args);

    protected abstract boolean isDelayable();


    public SppPlayer retrievePlayer(CommandSender sender, String[] args) {
        PlayerRetrievalStrategy strategy = getPlayerRetrievalStrategy();
        if (strategy == NONE) {
            return null;
        }

        Optional<SppPlayer> player = playerManager.getOnOrOfflinePlayer(getPlayerName(sender, args).get());
        if (!player.isPresent()) {
            throw new BusinessException(messages.playerNotRegistered);
        }

        switch (strategy) {
            case BOTH:
                return player.get();
            case ONLINE:
                if (!player.get().isOnline() && !shouldDelay(args)) {
                    throw new PlayerOfflineException();
                }
                return player.get();
            default:
                return null;
        }
    }


    private void validateAuthentication(CommandSender sender) {
        if (isAuthenticationRequired() && sender instanceof Player) {
            IocContainer.getAuthenticationService().checkAuthentication((Player) sender);
        }
    }

    private void validatePermissions(CommandSender sender) {
        if (getPermission() == null) {
            return;
        }
        if (!permission.has(sender, getPermission())) {
            throw new NoPermissionException(messages.prefixGeneral);
        }
    }

    private void validateMinimumArguments(CommandSender sender, String[] args) {
        if (args.length < getMinimumArguments(sender, args)) {
            throw new BusinessException(messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()), messages.prefixGeneral);
        }
    }

    protected List<String> getSppArguments(CommandSender sender, String[] args) {
        return Arrays.asList(Arrays.copyOfRange(args, getMinimumArguments(sender, args), args.length));
    }

    private boolean shouldDelay(String[] args) {
        return isDelayable() && Arrays.asList(args).contains(delayArgumentExecutor.getType().getPrefix());
    }

    private void delayCommand(CommandSender sender, String alias, String[] args, String playerName) {
        delayArgumentExecutor.execute(sender, playerName, getDelayedCommand(alias, args));
    }

    private String getDelayedCommand(String alias, String[] args) {
        return alias + " " + Stream.of(args).filter(a -> !a.equals(delayArgumentExecutor.getType().getPrefix())).collect(Collectors.joining(" "));
    }


}
