package net.shortninja.staffplus.core.common.cmd;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.authentication.AuthenticationService;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentProcessor;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.delayedactions.DelayArgumentExecutor;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@IocBean
public class CommandService {

    private final PermissionHandler permissionHandler;
    private final CommandPlayerRetriever commandPlayerRetriever;
    private final DelayArgumentExecutor delayArgumentExecutor;
    private final AuthenticationService authenticationService;
    private final ArgumentProcessor argumentProcessor;

    public CommandService(PermissionHandler permissionHandler, CommandPlayerRetriever commandPlayerRetriever, DelayArgumentExecutor delayArgumentExecutor, AuthenticationService authenticationService, ArgumentProcessor argumentProcessor) {
        this.permissionHandler = permissionHandler;
        this.commandPlayerRetriever = commandPlayerRetriever;
        this.delayArgumentExecutor = delayArgumentExecutor;
        this.authenticationService = authenticationService;
        this.argumentProcessor = argumentProcessor;
    }

    public void processArguments(CommandSender sender, String[] args, String playerName, List<ArgumentType> executionSppArguments) {
        argumentProcessor.parseArguments(sender, playerName, args, executionSppArguments);
    }

    public void validateAuthentication(boolean authenticate, CommandSender sender) {
        if (authenticate && sender instanceof Player) {
            authenticationService.checkAuthentication((Player) sender);
        }
    }

    public void validatePermissions(CommandSender sender, Set<String> permissions) {
        permissionHandler.validateAny(sender, permissions);
    }

    public Optional<SppPlayer> retrievePlayer(String[] args, String playerName, PlayerRetrievalStrategy playerRetrievalStrategy, boolean isDelayable) {
        return commandPlayerRetriever.retrievePlayer(playerRetrievalStrategy, playerName, shouldDelay(args, isDelayable));
    }

    public boolean shouldDelay(String[] args, boolean isDelayable) {
        return isDelayable && Arrays.asList(args).contains(delayArgumentExecutor.getType().getPrefix());
    }

    public void delayCommand(CommandSender sender, String alias, String[] args, String playerName) {
        String delayedCommand = alias + " " + Stream.of(args)
            .filter(a -> !a.equals(delayArgumentExecutor.getType().getPrefix()))
            .collect(Collectors.joining(" "));

        delayArgumentExecutor.execute(sender, playerName, delayedCommand);
    }

    public List<String> getSppArgumentsTabCompletion(List<ArgumentType> validTypes, String[] args) {
        return validTypes.stream().filter(t -> Arrays.stream(args).noneMatch(a -> a.startsWith(t.getPrefix())))
            .map(ArgumentType::getPrefix)
            .collect(Collectors.toList());
    }
    public List<String> getSppArgumentTabCompletion(String currentArg, ArgumentType argumentType) {
        return argumentProcessor.getTabCompletion(currentArg, argumentType);
    }
}
