package net.shortninja.staffplus.server.command.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.BusinessException;
import net.shortninja.staffplus.server.command.arguments.ArgumentType;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.util.lib.JavaUtils;
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

public class ClearInvCmd extends StaffPlusPlusCmd {
    private static final List<ArgumentType> VALID_ARGUMENTS = Arrays.asList(TELEPORT, HEALTH);

    private Messages messages = IocContainer.getMessages();

    public ClearInvCmd(String name) {
        super(name, IocContainer.getOptions().permissionClearInv);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args) {
        Player player = Bukkit.getServer().getPlayer(args[0]);
        if (player == null) {
            throw new BusinessException(messages.playerOffline, messages.prefixGeneral);
        }

        List<String> arguments = Arrays.asList(Arrays.copyOfRange(args, 1, args.length));

        JavaUtils.clearInventory(player);
        sender.sendMessage(player.getName() + "'s inventory has been cleared");
        argumentProcessor.parseArguments(sender, args[0], arguments, VALID_ARGUMENTS);
        return true;
    }

    @Override
    protected String getPlayerName(String[] args) {
        return args[0];
    }

    @Override
    protected int getMinimumArguments(String[] args) {
        return 1;
    }

    @Override
    protected boolean isDelayable() {
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> onlinePlayers = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        List<String> offlinePlayers = Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).collect(Collectors.toList());
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            suggestions.addAll(onlinePlayers);
            suggestions.addAll(offlinePlayers);
            return suggestions.stream()
                    .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                    .collect(Collectors.toList());
        }

        suggestions.addAll(argumentProcessor.getArgumentsSuggestions(sender, args[args.length - 1], VALID_ARGUMENTS));
        suggestions.add(DELAY.getPrefix());
        return suggestions;
    }
}
