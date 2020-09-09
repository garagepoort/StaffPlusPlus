package net.shortninja.staffplus.common;

import net.shortninja.staffplus.StaffPlus;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUtil {

    public static boolean executeCommand(CommandSender sender, CommandInterface commandInterface) {
        try {
            return commandInterface.execute();
        } catch (BusinessException e) {
            StaffPlus.get().message.send(sender, e.getMessage(), e.getPrefix());
            return false;
        }
    }
    public static void playerAction(Player player, PlayerActionInterface commandInterface) {
        try {
            commandInterface.execute();
        } catch (BusinessException e) {
            StaffPlus.get().message.send(player, e.getMessage(), e.getPrefix());
        }
    }

}
