package net.shortninja.staffplus.common.cmd;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import org.bukkit.entity.Player;

public class CommandUtil {

    public static void playerAction(Player player, PlayerActionInterface commandInterface) {
        try {
            commandInterface.execute();
        } catch (BusinessException e) {
            IocContainer.getMessage().send(player, e.getMessage(), e.getPrefix());
        }
    }

}
