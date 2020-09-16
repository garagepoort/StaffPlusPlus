package net.shortninja.staffplus.common;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.server.command.arguments.DelayArgumentExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandUtil {

    private static final DelayArgumentExecutor delayArgumentExecutor = new DelayArgumentExecutor();

    public static boolean executeCommand(CommandSender sender, CommandInterface commandInterface) {
        try {
            return commandInterface.execute();
        } catch (BusinessException e) {
            IocContainer.getMessage().send(sender, e.getMessage(), e.getPrefix());
            return false;
        }
    }

    public static void playerAction(Player player, PlayerActionInterface commandInterface) {
        try {
            commandInterface.execute();
        } catch (BusinessException e) {
            IocContainer.getMessage().send(player, e.getMessage(), e.getPrefix());
        }
    }

    public static String getDelayedCommand(String alias, String[] args) {
        return alias + " " + Stream.of(args).filter(a -> !a.equals(delayArgumentExecutor.getType().getPrefix())).collect(Collectors.joining(" "));
    }

    public static boolean shouldDelay(String[] args) {
        return Arrays.asList(args).contains(delayArgumentExecutor.getType().getPrefix());
    }

    public static void delayCommand(CommandSender sender, String alias, String[] args, String playerName) {
        delayArgumentExecutor.execute(sender, playerName, getDelayedCommand(alias, args));
    }
}
