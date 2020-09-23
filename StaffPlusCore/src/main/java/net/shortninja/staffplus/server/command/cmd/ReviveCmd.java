package net.shortninja.staffplus.server.command.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.BusinessException;
import net.shortninja.staffplus.common.PlayerOfflineException;
import net.shortninja.staffplus.player.attribute.mode.handler.ReviveHandler;
import net.shortninja.staffplus.server.command.arguments.ArgumentProcessor;
import net.shortninja.staffplus.server.command.arguments.ArgumentType;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.common.CommandUtil.executeCommand;
import static net.shortninja.staffplus.server.command.arguments.ArgumentType.*;
import static net.shortninja.staffplus.util.lib.JavaUtils.getTargetPlayer;
import static org.bukkit.Bukkit.getPlayer;

public class ReviveCmd extends BukkitCommand {
    private static final List<ArgumentType> VALID_ARGUMENTS = Arrays.asList(TELEPORT, STRIP, HEALTH);

    private final PermissionHandler permission = IocContainer.getPermissionHandler();
    private final MessageCoordinator message = IocContainer.getMessage();
    private final Options options = IocContainer.getOptions();
    private final Messages messages = IocContainer.getMessages();
    private final ReviveHandler reviveHandler = StaffPlus.get().reviveHandler;
    private final ArgumentProcessor argumentProcessor = ArgumentProcessor.getInstance();

    public ReviveCmd(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        return executeCommand(sender, () -> {

            if (!permission.has(sender, options.permissionRevive)) {
                throw new BusinessException(messages.noPermission, messages.prefixGeneral);
            }

            List<String> nonArguments = Arrays.stream(args).filter(a -> !a.startsWith("-")).collect(Collectors.toList());
            if (nonArguments.size() == 0 && !(sender instanceof Player)) {
                throw new BusinessException(messages.invalidArguments, messages.prefixGeneral);
            }

            Player targetPlayer = nonArguments.size() == 1 ? getPlayer(nonArguments.get(0)) : getTargetPlayer((Player) sender);
            if (targetPlayer == null) {
                throw new PlayerOfflineException();
            }

            if (reviveHandler.hasSavedInventory(targetPlayer.getUniqueId())) {
                reviveHandler.restoreInventory(targetPlayer);
                argumentProcessor.parseArguments(sender, targetPlayer.getName(), Arrays.asList(args), VALID_ARGUMENTS);
                message.send(sender, messages.revivedStaff.replace("%target%", targetPlayer.getName()), messages.prefixGeneral);
            } else {
                message.send(sender, messages.noFound, messages.prefixGeneral);
            }

            return true;
        });
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> onlinePlayers = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            suggestions.addAll(onlinePlayers);
            return suggestions;
        }

        suggestions.addAll(argumentProcessor.getArgumentsSuggestions(sender, args[args.length-1], VALID_ARGUMENTS));
        return  suggestions;
    }
}