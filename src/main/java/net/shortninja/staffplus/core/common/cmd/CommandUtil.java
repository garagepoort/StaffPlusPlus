package net.shortninja.staffplus.core.common.cmd;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;

import org.bukkit.entity.Player;

public class CommandUtil {

    public static void playerAction(Player player, PlayerActionInterface commandInterface) {
        try {
            commandInterface.execute();
        } catch (BusinessException e) {
            StaffPlus.get().getIocContainer().get(Messages.class).send(player, e.getMessage(), e.getPrefix());
        }
    }

}
