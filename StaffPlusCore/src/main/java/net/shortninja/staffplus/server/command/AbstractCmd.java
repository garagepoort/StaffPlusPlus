package net.shortninja.staffplus.server.command;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.BusinessException;
import net.shortninja.staffplus.common.NoPermissionException;
import net.shortninja.staffplus.common.PlayerOfflineException;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.arguments.ArgumentProcessor;
import net.shortninja.staffplus.server.command.arguments.ArgumentType;
import net.shortninja.staffplus.server.command.arguments.DelayArgumentExecutor;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static net.shortninja.staffplus.server.command.PlayerRetrievalStrategy.NONE;
import static net.shortninja.staffplus.server.command.arguments.ArgumentType.DELAY;

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

            validateExecution(player);
            processArguments(sender, args, getPreExecutionSppArguments());
            boolean result = executeCmd(sender, alias, args, player);
            processArguments(sender, args, getPostExecutionSppArguments());
            return result;
        } catch (BusinessException e) {
            IocContainer.getMessage().send(sender, e.getMessage(), e.getPrefix());
            return false;
        }
    }


    /**
     * If your command requires extra custom validation apart from the one provided by the AbstractCmd class,
     * you can add them by overriding this method. Validation will be executed right before preSppArgument execution.
     */
    protected void validateExecution(SppPlayer player) {
        //No execution by default
    }

    private void processArguments(CommandSender sender, String[] args, List<ArgumentType> executionSppArguments) {
        List<String> sppArguments = getSppArguments(sender, args);
        argumentProcessor.parseArguments(sender, getPlayerName(sender, args).orElse(null), sppArguments, executionSppArguments);
    }

    /**
     * @return List of arguments that are valid to be used in combination with this command,
     * These commands, if present, will be executed before the `executeCmd` method
     */
    protected List<ArgumentType> getPreExecutionSppArguments() {
        return Collections.emptyList();
    }

    /**
     * @return List of arguments that are valid to be used in combination with this command,
     * These commands, if present, will be executed after the `executeCmd` method
     */
    protected List<ArgumentType> getPostExecutionSppArguments() {
        return Collections.emptyList();
    }

    protected List<String> getSppArgumentsSuggestions(CommandSender sender, String[] args) {
        List<ArgumentType> validArguments = Stream.concat(getPreExecutionSppArguments().stream(), getPostExecutionSppArguments().stream())
            .collect(toList());

        List<String> suggestions = new ArrayList<>(argumentProcessor.getArgumentsSuggestions(sender, args[args.length - 1], validArguments));
        if (isDelayable()) {
            suggestions.add(DELAY.getPrefix());
        }
        return suggestions;
    }

    /**
     * @param player The targeted player if there is any. If no player is targeted this method will not be executed
     * @return boolean indicating if the targeted player cna bypass this command. If the player by passes it,
     * a message is shown to the executor and nothing happens
     */
    protected boolean canBypass(Player player) {
        return false;
    }

    protected abstract boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player);

    /**
     * @param sender CommandSender
     * @param args   The original args passed to the command
     * @return Integer indicating the minimum amount of arguments that should be present.
     * if this value is not reached an exception will be thrown
     */
    protected abstract int getMinimumArguments(CommandSender sender, String[] args);

    /**
     * @return boolean indicating if an authentication check should be triggered. If AuthMe or another authenticationProvider is configured then this
     * command might stop exection if the sender is not logged in.
     */
    protected boolean isAuthenticationRequired() {
        return true;
    }

    /**
     * Determines when the command should throw an error while finding the targeted player.
     * Use NONE, if this command does not target a player. No lookup of the player will be done
     * Use ONLINE, if this cmd only targets online players. If the player can't be found or if he is online an exception will be thrown
     * If the `DELAY` flag is set this command won't throw an error if the player is found but offline.
     * Use BOTH, if this command targets online and offline users. An exception will be thrown if the user is not known to the server.
     *
     * @return PlayerRetrievalStrategy NONE|ONLINE|BOTH
     */
    protected abstract PlayerRetrievalStrategy getPlayerRetrievalStrategy();

    /**
     * Used to find the player before running the `executeCmd` method
     *
     * @param sender CommandSender
     * @param args   The original args passed to the command
     * @return The playerName, usually based on the given args. If this command does not target a player return an empty Optional
     */
    protected abstract Optional<String> getPlayerName(CommandSender sender, String[] args);

    /**
     * @return boolean Indicates if this method can be delayed until the next login of a player
     */
    protected boolean isDelayable() {
        return false;
    }

    private SppPlayer retrievePlayer(CommandSender sender, String[] args) {
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
