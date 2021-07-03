package net.shortninja.staffplus.core.common.cmd;

import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public abstract class AbstractCmd extends BukkitCommand implements SppCommand {

    protected final Messages messages;
    protected final Options options;
    private final CommandService commandService;

    private Set<String> permissions = new HashSet<>();

    protected AbstractCmd(String name, Messages messages, Options options, CommandService commandService) {
        super(name);
        this.messages = messages;

        this.options = options;
        this.commandService = commandService;
    }

    protected void validateIsPlayer(CommandSender sender) {
        if (!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        try {
            commandService.validateAuthentication(isAuthenticationRequired(), sender);
            commandService.validatePermissions(sender, permissions);
            String[] sppArgs = Arrays.stream(args).filter(a -> getSppArguments().stream().map(ArgumentType::getPrefix).anyMatch(a::startsWith)).toArray(String[]::new);
            String[] filteredArgs = Arrays.stream(args).filter(a -> getSppArguments().stream().map(ArgumentType::getPrefix).noneMatch(a::startsWith)).toArray(String[]::new);

            validateMinimumArguments(sender, filteredArgs);

            PlayerRetrievalStrategy strategy = getPlayerRetrievalStrategy();
            String playerName = getPlayerName(sender, filteredArgs).orElse(null);

            Optional<SppPlayer> player = commandService.retrievePlayer(sppArgs, playerName, strategy, isDelayable());

            if (player.isPresent()) {
                if (player.get().isOnline() && canBypass(player.get().getPlayer())) {
                    throw new BusinessException(messages.bypassed, messages.prefixGeneral);
                }
                if (commandService.shouldDelay(sppArgs, isDelayable())) {
                    commandService.delayCommand(sender, alias, filteredArgs, player.get().getUsername());
                    return true;
                }
            }

            validateExecution(player.orElse(null));

            commandService.processArguments(sender, sppArgs, playerName, getPreExecutionSppArguments());
            boolean result = executeCmd(sender, alias, filteredArgs, player.orElse(null));
            commandService.processArguments(sender, sppArgs, playerName, getPostExecutionSppArguments());
            return result;
        } catch (BusinessException e) {
            messages.send(sender, e.getMessage(), e.getPrefix());
            return false;
        }
    }

    @Override
    public void setPermission(String permission) {
        this.permissions.add(permission);
    }

    private List<ArgumentType> getSppArguments() {
        List<ArgumentType> collect = Stream.of(getPreExecutionSppArguments(), getPostExecutionSppArguments()).flatMap(Collection::stream).collect(Collectors.toList());
        if(isDelayable()) {
            collect.add(ArgumentType.DELAY);
        }
        return collect;
    }
    /**
     * If your command requires extra custom validation apart from the one provided by the AbstractCmd class,
     * you can add them by overriding this method. Validation will be executed right before preSppArgument execution.
     */
    protected void validateExecution(SppPlayer player) {
        //No execution by default
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

    private void validateMinimumArguments(CommandSender sender, String[] args) {
        if (args.length < getMinimumArguments(sender, args)) {
            throw new BusinessException(messages.invalidArguments.replace("%usage%", " &7" + getUsage()));
        }
    }

    protected void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        String[] filteredArgs = Arrays.stream(args).filter(a -> getSppArguments().stream().map(ArgumentType::getPrefix).noneMatch(a::startsWith)).toArray(String[]::new);
        String[] sppArgs = Arrays.stream(args).filter(a -> getSppArguments().stream().map(ArgumentType::getPrefix).anyMatch(a::startsWith)).toArray(String[]::new);

        String currentArg = args.length > 0 ? args[args.length - 1] : "";
        Optional<ArgumentType> matchedArgType = getSppArguments().stream().filter(a -> currentArg.startsWith(a.getPrefix())).findFirst();
        if(matchedArgType.isPresent()) {
            return commandService.getSppArgumentTabCompletion(currentArg, matchedArgType.get());
        }

        List<String> tabResults = autoComplete(sender, filteredArgs, sppArgs);
        if(tabResults.isEmpty()) {
            return commandService.getSppArgumentsTabCompletion(getSppArguments(), args);
        }
        return tabResults;
    }

    protected List<String> autoComplete(CommandSender sender, String[] args, String[] sppArgs) throws IllegalArgumentException {
        return Collections.emptyList();
    }
}
