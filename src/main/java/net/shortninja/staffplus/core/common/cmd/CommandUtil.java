package net.shortninja.staffplus.core.common.cmd;

import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import org.bukkit.entity.Player;

public class CommandUtil {

    public static void playerAction(Player player, PlayerActionInterface commandInterface) {
        try {
            commandInterface.execute();
        } catch (BusinessException e) {
            IocContainer.get(MessageCoordinator.class).send(player, e.getMessage(), e.getPrefix());
        }
    }

}
