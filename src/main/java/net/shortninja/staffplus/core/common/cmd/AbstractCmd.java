package net.shortninja.staffplus.core.common.cmd;

import net.shortninja.staffplus.core.StaffPlusPlus;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.NullPointerException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.bukkit.Bukkit.getScheduler;

public abstract class AbstractCmd extends BukkitCommand implements SppCommand {

    private static final String SPACE_REPLACER = "%space%";
    protected final Messages messages;
    protected final PermissionHandler permissionHandler;
    private final CommandService commandService;
    private boolean delayable;
    private boolean replaceDoubleQoutesEnabled;
    private PlayerRetrievalStrategy playerRetrievalStrategy = PlayerRetrievalStrategy.NONE;
    private Set<String> permissions = new HashSet<>();
    private final Map<UUID, Long> lastUse = new HashMap<>();
    private boolean async;

    protected AbstractCmd(String name, Messages messages, PermissionHandler permissionHandler, CommandService commandService) {
        super(name);
        this.messages = messages;
        this.permissionHandler = permissionHandler;
        this.commandService = commandService;
    }

    protected AbstractCmd(Messages messages, PermissionHandler permissionHandler, CommandService commandService) {
        super("");
        this.messages = messages;
        this.permissionHandler = permissionHandler;
        this.commandService = commandService;
    }

    protected Player validateIsPlayer(CommandSender sender) {
        if (!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }
        return (Player) sender;
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        try {
            if (args.length > 0 && replaceDoubleQoutesEnabled) {
                args = replaceDoubleQoutes(args);
            }

            commandService.validateAuthentication(isAuthenticationRequired(), sender);
            commandService.validatePermissions(sender, permissions);
            validateCoolDown(sender);

            String[] optionalParamaters = Arrays.stream(args).filter(a -> getOptionalParameters().stream().anyMatch(a::startsWith)).toArray(String[]::new);
            String[] filteredArgs = Arrays.stream(args).filter(a -> getOptionalParameters().stream().noneMatch(a::startsWith)).toArray(String[]::new);
            String[] sppArgs = Arrays.stream(filteredArgs).filter(a -> getSppArguments().stream().map(ArgumentType::getPrefix).anyMatch(a::startsWith)).toArray(String[]::new);
            filteredArgs = Arrays.stream(filteredArgs).filter(a -> getSppArguments().stream().map(ArgumentType::getPrefix).noneMatch(a::startsWith)).toArray(String[]::new);

            validateMinimumArguments(sender, filteredArgs);

            String playerName = getPlayerName(sender, filteredArgs).orElse(null);

            Optional<SppPlayer> player = commandService.retrievePlayer(sppArgs, playerName, getPlayerRetrievalStrategy(), delayable);
            
            if (player == null) {
                throw new BusinessException(messages.invalidArguments.replace("%usage%", " &7" + getUsage()));
            }

            if (player.isPresent()) {
                if (player.get().isOnline() && canBypass(player.get().getPlayer())) {
                    throw new BusinessException(messages.bypassed, messages.prefixGeneral);
                }
                if (commandService.shouldDelay(sppArgs, delayable)) {
                    commandService.delayCommand(sender, alias, filteredArgs, player.get().getUsername());
                    return true;
                }
            }

            validateExecution(player.orElse(null));

            commandService.processArguments(sender, sppArgs, playerName, getPreExecutionSppArguments());
            boolean result = true;
            if (!async) {
                result = executeCmd(sender, alias, filteredArgs, player.orElse(null), mapOptionalParameters(optionalParamaters));
            } else {
                String[] finalFilteredArgs = filteredArgs;
                getScheduler().runTaskAsynchronously(StaffPlusPlus.get(), () -> {
                    try {
                        executeCmd(sender, alias, finalFilteredArgs, player.orElse(null), mapOptionalParameters(optionalParamaters));
                    } catch (BusinessException e) {
                        messages.send(sender, e.getMessage(), e.getPrefix());
                    }
                });
            }
            commandService.processArguments(sender, sppArgs, playerName, getPostExecutionSppArguments());
            if (sender instanceof Player) {
                lastUse.put(((Player) sender).getUniqueId(), System.currentTimeMillis());
            }
            return result;
        } catch (BusinessException e) {
            messages.send(sender, e.getMessage(), e.getPrefix());
            return false;
        }
    }

    @NotNull
    private String[] replaceDoubleQoutes(String[] args) {
        String joined = String.join(" ", args);
        List<String> allMatches = getAllMatches(joined, "(\"(?:[^\"\\\\\n]|\\\\.)*\")");
        for (String match : allMatches) {
            joined = joined.replace(match, match.replaceAll("\\s+", SPACE_REPLACER));
        }
        return joined.split(" ");
    }

    private void validateCoolDown(CommandSender sender) {
        if (sender instanceof Player) {
            Optional<Long> cooldown = permissionHandler.getDurationInSeconds(sender, "staff." + getName() + ".cooldown");
            if (cooldown.isPresent() && lastUse.containsKey(((Player) sender).getUniqueId())) {
                long last = lastUse.get(((Player) sender).getUniqueId());
                long secondsOnCooldown = (System.currentTimeMillis() - last) / 1000;

                if (secondsOnCooldown < cooldown.get()) {
                    throw new BusinessException(messages.commandOnCooldown.replace("%seconds%", Long.toString(cooldown.get() - secondsOnCooldown)), messages.prefixGeneral);
                }
            }
        }
    }

    private Map<String, String> mapOptionalParameters(String[] optionalParamaters) {
        Map<String, String> result = new HashMap<>();
        for (String optionalParamater : optionalParamaters) {
            String[] templateParams = optionalParamater.split("=");
            if (templateParams.length != 2) {
                result.put(templateParams[0], null);
            } else {
                result.put(templateParams[0], templateParams[1].replaceAll("^\"|\"$", "").replace(SPACE_REPLACER, " "));
            }
        }
        return result;
    }

    @Override
    public void setPermission(String permission) {
        this.permissions.add(permission);
    }

    private List<ArgumentType> getSppArguments() {
        List<ArgumentType> collect = Stream.of(getPreExecutionSppArguments(), getPostExecutionSppArguments()).flatMap(Collection::stream).collect(Collectors.toList());
        if (delayable) {
            collect.add(ArgumentType.DELAY);
        }
        return collect;
    }

    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return playerRetrievalStrategy;
    }

    /**
     * If your command requires extra custom validation apart from the one provided by the AbstractCmd class,
     * you can add them by overriding this method. Validation will be executed right before preSppArgument execution.
     */
    protected void validateExecution(SppPlayer player) {
        //No execution by default
    }

    protected List<String> getOptionalParameters() {
        return Collections.emptyList();
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

    protected abstract boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters);

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
     * OPTIONAL_BOTH is either NONE or BOTH depending on if the getPlayerName() method returns a playername or not
     * OPTIONAL_ONLINE is either NONE or ONLINE depending on if the getPlayerName() method returns a playername or not
     */
    public void setPlayerRetrievalStrategy(PlayerRetrievalStrategy playerRetrievalStrategy) {
        this.playerRetrievalStrategy = playerRetrievalStrategy;
    }

    /**
     * Used to find the player before running the `executeCmd` method
     *
     * @param sender CommandSender
     * @param args   The original args passed to the command
     * @return The playerName, usually based on the given args. If this command does not target a player return an empty Optional
     */
    protected abstract Optional<String> getPlayerName(CommandSender sender, String[] args);

    public void setDelayable(boolean delayable) {
        this.delayable = delayable;
    }

    public void setReplaceDoubleQoutesEnabled(boolean replaceDoubleQoutesEnabled) {
        this.replaceDoubleQoutesEnabled = replaceDoubleQoutesEnabled;
    }

    private void validateMinimumArguments(CommandSender sender, String[] args) {
        if (args.length < getMinimumArguments(sender, args)) {
            throw new BusinessException(messages.invalidArguments.replace("%usage%", " &7" + getUsage()));
        }
    }

    protected void setPermissions(Set<String> permissions) {
        if (!permissions.isEmpty()) {
            super.setPermission(permissions.iterator().next());
        }
        this.permissions = permissions;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        String[] filteredArgs = Arrays.stream(args).filter(a -> getSppArguments().stream().map(ArgumentType::getPrefix).noneMatch(a::startsWith)).toArray(String[]::new);
        String[] sppArgs = Arrays.stream(args).filter(a -> getSppArguments().stream().map(ArgumentType::getPrefix).anyMatch(a::startsWith)).toArray(String[]::new);

        String currentArg = args.length > 0 ? args[args.length - 1] : "";
        Optional<ArgumentType> matchedArgType = getSppArguments().stream().filter(a -> currentArg.startsWith(a.getPrefix())).findFirst();
        if (matchedArgType.isPresent()) {
            return commandService.getSppArgumentTabCompletion(currentArg, matchedArgType.get());
        }

        List<String> tabResults = autoComplete(sender, filteredArgs, sppArgs);
        if (tabResults.isEmpty()) {
            return commandService.getSppArgumentsTabCompletion(getSppArguments(), args);
        }
        return tabResults;
    }

    protected List<String> autoComplete(CommandSender sender, String[] args, String[] sppArgs) throws IllegalArgumentException {
        return Collections.emptyList();
    }

    private List<String> getAllMatches(String text, String regex) {
        List<String> matches = new ArrayList<>();
        Matcher m = Pattern.compile("(?=(" + regex + "))").matcher(text);
        while (m.find()) {
            matches.add(m.group(1));
        }
        return matches;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }
}
