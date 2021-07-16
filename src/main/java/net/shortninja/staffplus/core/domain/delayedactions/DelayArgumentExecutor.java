package net.shortninja.staffplus.core.domain.delayedactions;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.delayedactions.database.DelayedActionsRepository;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@IocBean
public class DelayArgumentExecutor {

    private final Messages messages;
    private final PlayerManager playerManager;


    public DelayArgumentExecutor(Messages messages, PlayerManager playerManager) {
        this.messages = messages;
        this.playerManager = playerManager;

    }

    public boolean execute(CommandSender commandSender, String playerName, String command) {
        Optional<SppPlayer> player = playerManager.getOnOrOfflinePlayer(playerName);

        if (!player.isPresent()) {
            throw new BusinessException("&CCannot delay the command. No user found on this server with name: [" + playerName + "]", messages.prefixGeneral);
        }

        StaffPlus.get().getIocContainer().get(DelayedActionsRepository.class).saveDelayedAction(player.get().getId(), command, Executor.CONSOLE);
        messages.send(commandSender, "Your command has been delayed and will be executed next time [" + playerName + "] joins the server", messages.prefixGeneral);
        return true;
    }

    public ArgumentType getType() {
        return ArgumentType.DELAY;
    }

    public List<String> complete() {
        return Arrays.asList(getType().getPrefix());
    }
}
