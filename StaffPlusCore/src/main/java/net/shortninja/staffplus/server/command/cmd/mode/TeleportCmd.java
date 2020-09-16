package net.shortninja.staffplus.server.command.cmd.mode;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.BusinessException;
import net.shortninja.staffplus.server.command.arguments.ArgumentProcessor;
import net.shortninja.staffplus.server.command.arguments.ArgumentType;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.teleport.TeleportService;
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
import static net.shortninja.staffplus.server.command.arguments.ArgumentType.HEALTH;
import static net.shortninja.staffplus.server.command.arguments.ArgumentType.STRIP;

public class TeleportCmd extends BukkitCommand {
    private static final List<ArgumentType> VALID_ARGUMENTS = Arrays.asList(STRIP, HEALTH);

    private Messages messages = IocContainer.getMessages();
    private ArgumentProcessor argumentProcessor = ArgumentProcessor.getInstance();
    private final Options options = IocContainer.getOptions();

    public TeleportCmd(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        return executeCommand(sender, () -> {
            if (args.length < 2) {
                throw new BusinessException(messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()), messages.prefixGeneral);
            }
            List<String> options = Arrays.asList(Arrays.copyOfRange(args, 2, args.length));

            Player targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                throw new BusinessException(messages.playerOffline, messages.prefixGeneral);
            }

            TeleportService.getInstance().teleportPlayer(sender, targetPlayer, args[1]);
            argumentProcessor.parseArguments(sender, args[0], options, VALID_ARGUMENTS);

            return true;
        });
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            List<String> onlinePlayers = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
            suggestions.addAll(onlinePlayers);
            return suggestions.stream()
                    .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                    .collect(Collectors.toList());
        }
        if (args.length == 2) {
            options.locations.forEach((k,v) -> {
                suggestions.add(k);
            });
            return suggestions.stream()
                    .filter(s -> args[1].isEmpty() || s.contains(args[1]))
                    .collect(Collectors.toList());
        }

        suggestions.addAll(argumentProcessor.getArgumentsSuggestions(sender, args[args.length-1], VALID_ARGUMENTS));
        return suggestions;
    }
}