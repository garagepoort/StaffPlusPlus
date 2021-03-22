package net.shortninja.staffplus.core.domain.delayedactions;

import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.domain.delayedactions.database.DelayedActionsRepository;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DelayArgumentExecutor {

    private final Messages messages;
    private final PlayerManager playerManager;
    private final MessageCoordinator message;

    public DelayArgumentExecutor() {
        messages = IocContainer.get(Messages.class);
        playerManager = IocContainer.get(PlayerManager.class);
        message = IocContainer.get(MessageCoordinator.class);
    }

    public boolean execute(CommandSender commandSender, String playerName, String command) {
        Optional<SppPlayer> player = playerManager.getOnOrOfflinePlayer(playerName);

        if (!player.isPresent()) {
            throw new BusinessException("&CCannot delay the command. No user found on this server with name: [" + playerName + "]", messages.prefixGeneral);
        }

        IocContainer.get(DelayedActionsRepository.class).saveDelayedAction(player.get().getId(), command, Executor.CONSOLE);
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
