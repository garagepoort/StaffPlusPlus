package net.shortninja.staffplus.core.common.cmd;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import org.bukkit.entity.Player;

@IocBean
public class CommandUtil {

    private final Messages messages;

    public CommandUtil(Messages messages) {
        this.messages = messages;
    }

    public void playerAction(Player player, PlayerActionInterface commandInterface) {
        try {
            commandInterface.execute();
        } catch (BusinessException e) {
            messages.send(player, e.getMessage(), e.getPrefix());
        }
    }

}
