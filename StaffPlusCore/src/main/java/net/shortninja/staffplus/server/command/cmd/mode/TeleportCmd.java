package net.shortninja.staffplus.server.command.cmd.mode;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.BusinessException;
import net.shortninja.staffplus.server.command.arguments.ArgumentProcessor;
import net.shortninja.staffplus.server.command.arguments.ArgumentType;
import net.shortninja.staffplus.server.command.cmd.StaffPlusPlusCmd;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.staff.teleport.TeleportService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.server.command.arguments.ArgumentType.*;

public class TeleportCmd extends StaffPlusPlusCmd {
    private static final List<ArgumentType> VALID_ARGUMENTS = Arrays.asList(STRIP, HEALTH);

    private Messages messages = IocContainer.getMessages();
    private ArgumentProcessor argumentProcessor = ArgumentProcessor.getInstance();

    public TeleportCmd(String name) {
        super(name, IocContainer.getOptions().permissionTeleport);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args) {
        List<String> options = Arrays.asList(Arrays.copyOfRange(args, getMinimumArguments(args), args.length));

        String locationName = args[1];
        String playerName = getPlayerName(args);

        Player targetPlayer = Bukkit.getPlayer(playerName);
        if (targetPlayer == null) {
            throw new BusinessException(messages.playerOffline, messages.prefixGeneral);
        }

        TeleportService.getInstance().teleportPlayer(sender, targetPlayer, locationName);
        argumentProcessor.parseArguments(sender, playerName, options, VALID_ARGUMENTS);
        return true;
    }

    @Override
    protected String getPlayerName(String[] args) {
        return args[0];
    }

    @Override
    protected int getMinimumArguments(String[] args) {
        return 2;
    }

    @Override
    protected boolean isDelayable() {
        return true;
    }

    @Override
    protected boolean canBypass(Player player) {
        return permission.has(player, options.permissionTeleportBypass);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            List<String> onlinePlayers = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
            List<String> offlinePlayers = Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).collect(Collectors.toList());
            suggestions.addAll(onlinePlayers);
            suggestions.addAll(offlinePlayers);
            return suggestions.stream()
                    .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                    .collect(Collectors.toList());
        }
        if (args.length == 2) {
            IocContainer.getOptions().locations.forEach((k, v) -> {
                suggestions.add(k);
            });
            return suggestions.stream()
                    .filter(s -> args[1].isEmpty() || s.contains(args[1]))
                    .collect(Collectors.toList());
        }

        suggestions.addAll(argumentProcessor.getArgumentsSuggestions(sender, args[args.length-1], VALID_ARGUMENTS));
        suggestions.add(DELAY.getPrefix());
        return suggestions;
    }
}