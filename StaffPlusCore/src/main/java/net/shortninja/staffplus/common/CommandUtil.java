package net.shortninja.staffplus.common;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.server.command.arguments.DelayArgumentExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

}
