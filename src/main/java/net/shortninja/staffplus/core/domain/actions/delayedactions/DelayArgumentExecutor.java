package net.shortninja.staffplus.core.domain.actions.delayedactions;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
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
    private final BukkitUtils bukkitUtils;
    private final ActionService actionService;


    public DelayArgumentExecutor(Messages messages, PlayerManager playerManager, Options options, BukkitUtils bukkitUtils, ActionService actionService) {
        this.messages = messages;
        this.playerManager = playerManager;
        this.options = options;
        this.bukkitUtils = bukkitUtils;
        this.actionService = actionService;
    }

    public boolean execute(CommandSender commandSender, String playerName, String command) {
        Optional<SppPlayer> player = playerManager.getOnOrOfflinePlayer(playerName);

        if (!player.isPresent()) {
            throw new BusinessException(messages.get("delay-command-player-not-found", "%player%", playerName), messages.prefixGeneral);
        }

        bukkitUtils.runTaskAsync(() -> actionService.createCommand(
            commandBuilder()
                .serverName(options.serverName)
                .executor(CONSOLE_UUID)
                .executorRunStrategy(ONLINE)
                .target(player.get().getOfflinePlayer())
                .targetRunStrategy(DELAY)
                .command(command)
                .build()));
        messages.sendTranslation(commandSender, "delay-command-created", messages.prefixGeneral, "%player%", playerName);
        return true;
    }

    public ArgumentType getType() {
        return ArgumentType.DELAY;
    }

    public List<String> complete() {
        return Arrays.asList(getType().getPrefix());
    }
}
