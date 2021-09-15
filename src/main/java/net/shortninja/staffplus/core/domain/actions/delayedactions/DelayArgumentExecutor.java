package net.shortninja.staffplus.core.domain.actions.delayedactions;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;
import static net.shortninja.staffplus.core.domain.actions.ActionRunStrategy.DELAY;
import static net.shortninja.staffplus.core.domain.actions.ActionRunStrategy.ONLINE;
import static net.shortninja.staffplus.core.domain.actions.CreateStoredCommandRequest.CreateStoredCommandRequestBuilder.commandBuilder;

@IocBean
public class DelayArgumentExecutor {

    private final Messages messages;
    private final PlayerManager playerManager;
    private final Options options;


    public DelayArgumentExecutor(Messages messages, PlayerManager playerManager, Options options) {
        this.messages = messages;
        this.playerManager = playerManager;
        this.options = options;
    }

    public boolean execute(CommandSender commandSender, String playerName, String command) {
        Optional<SppPlayer> player = playerManager.getOnOrOfflinePlayer(playerName);

        if (!player.isPresent()) {
            throw new BusinessException("&CCannot delay the command. No user found on this server with name: [" + playerName + "]", messages.prefixGeneral);
        }

        StaffPlus.get().getIocContainer().get(ActionService.class).createCommand(
            commandBuilder()
                .serverName(options.serverName)
                .executor(CONSOLE_UUID)
                .executorRunStrategy(ONLINE)
                .target(player.get().getOfflinePlayer())
                .targetRunStrategy(DELAY)
                .command(command)
                .build());
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
