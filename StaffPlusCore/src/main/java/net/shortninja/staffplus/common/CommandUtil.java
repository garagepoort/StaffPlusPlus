package net.shortninja.staffplus.common;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUtil {

    public static boolean executeCommand(CommandSender sender, boolean authenticated, CommandInterface commandInterface) {
        try {
            if(authenticated && sender instanceof Player) {
                IocContainer.getAuthenticationService().checkAuthentication((Player) sender);
            }
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
