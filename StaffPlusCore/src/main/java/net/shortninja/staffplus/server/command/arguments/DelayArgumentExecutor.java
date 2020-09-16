package net.shortninja.staffplus.server.command.arguments;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.BusinessException;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class DelayArgumentExecutor {

    private Messages messages;
    private UserManager userManager;
    private MessageCoordinator message;

    public DelayArgumentExecutor() {
        messages = IocContainer.getMessages();
        userManager = IocContainer.getUserManager();
        message = IocContainer.getMessage();
    }

    public boolean execute(CommandSender commandSender, String playerName, String command) {
        IUser user = userManager.getOnOrOfflineUser(playerName);

        if (user == null) {
            throw new BusinessException("Cannot delay the command. No user found on this server with name: [" + playerName + "]", messages.prefixGeneral);
        }

        IocContainer.getDelayedActionsRepository().saveDelayedAction(user.getUuid(), command);
        message.send(commandSender, "Your command has been delayed and will be executed next time [" + playerName + "] joins the server", messages.prefixGeneral);
        return true;
    }

    public ArgumentType getType() {
        return ArgumentType.DELAY;
    }

    public List<String> complete() {
        return Arrays.asList(getType().getPrefix());
    }
}
