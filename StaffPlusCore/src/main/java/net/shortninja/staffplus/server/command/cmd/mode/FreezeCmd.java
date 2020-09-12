package net.shortninja.staffplus.server.command.cmd.mode;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.BusinessException;
import net.shortninja.staffplus.player.attribute.mode.handler.freeze.FreezeHandler;
import net.shortninja.staffplus.player.attribute.mode.handler.freeze.FreezeRequest;
import net.shortninja.staffplus.server.command.arguments.ArgumentProcessor;
import net.shortninja.staffplus.server.command.arguments.ArgumentType;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.common.CommandUtil.executeCommand;
import static net.shortninja.staffplus.server.command.arguments.ArgumentType.*;

public class FreezeCmd extends BukkitCommand {
    private static final List<ArgumentType> VALID_ARGUMENTS = Arrays.asList(TELEPORT, STRIP, HEALTH);

    private PermissionHandler permission = StaffPlus.get().permission;
    private Messages messages = IocContainer.getMessages();
    private FreezeHandler freezeHandler = StaffPlus.get().freezeHandler;
    private ArgumentProcessor argumentProcessor = ArgumentProcessor.getInstance();

    public FreezeCmd(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        return executeCommand(sender, () -> {
            if (args.length < 1) {
                throw new BusinessException(messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()), messages.prefixGeneral);
            }
            List<String> options = Arrays.asList(Arrays.copyOfRange(args, 1, args.length));
            if (options.size() > 2) {
                throw new BusinessException(messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()), messages.prefixGeneral);
            }

            Player targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                throw new BusinessException(messages.playerOffline, messages.prefixGeneral);
            }

            freezeHandler.validatePermissions(sender, targetPlayer);
            argumentProcessor.parseArguments(sender, args[0], options, VALID_ARGUMENTS);
            if (options.isEmpty()) {
                // No options given, simple freeze
                freezeHandler.execute(new FreezeRequest(sender, targetPlayer, !freezeHandler.isFrozen(targetPlayer.getUniqueId())));
            } else {
                freezeHandler.execute(buildFreezeRequest(sender, options, targetPlayer));
            }


            return true;
        });
    }

    private FreezeRequest buildFreezeRequest(CommandSender sender, List<String> options, Player targetPlayer) {
        Optional<String> enabled = options.stream().filter(o -> o.equals("enabled")).findFirst();
        Optional<String> disabled = options.stream().filter(o -> o.startsWith("disabled")).findFirst();

        if ((enabled.isPresent() || disabled.isPresent()) && !permission.isOp(sender)) {
            throw new BusinessException(messages.noPermission, messages.prefixGeneral);
        }

        boolean freeze = enabled.isPresent() || (!disabled.isPresent() && !freezeHandler.isFrozen(targetPlayer.getUniqueId()));

        return new FreezeRequest(
                sender,
                targetPlayer,
                freeze
        );
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        boolean hasPermission = permission.isOp(sender);

        List<String> onlinePlayers = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            // /freeze <>
            if (!args[0].equals("enabled") && !args[0].equals("disabled")) {
                if (hasPermission) {
                    suggestions.add("enabled");
                    suggestions.add("disabled");
                }

                suggestions.addAll(onlinePlayers);
                return suggestions;
            } else {
                //freeze <>
                suggestions.addAll(onlinePlayers);
                return suggestions;
            }
        }

        if(args.length > 1) {
            if (args[0].equals("enabled") || args[0].equals("disabled")) {
                //freeze enabled <>
                suggestions.addAll(onlinePlayers);
                return suggestions;
            } else {
                //freeze playername <>
                suggestions.addAll(argumentProcessor.getArgumentsSuggestions(sender, args[args.length-1], VALID_ARGUMENTS));
                return  suggestions;
            }
        }

        return super.tabComplete(sender, alias, args);
    }
}