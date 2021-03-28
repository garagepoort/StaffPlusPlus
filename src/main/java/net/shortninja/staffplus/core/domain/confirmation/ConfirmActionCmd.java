package net.shortninja.staffplus.core.domain.confirmation;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

@IocBean
@IocMultiProvider(SppCommand.class)
public class ConfirmActionCmd extends AbstractCmd {

    private final ConfirmationChatService confirmationChatService;

    public ConfirmActionCmd(Messages messages, MessageCoordinator message, Options options, ConfirmationChatService confirmationChatService, CommandService commandService) {
        super("confirm-action", messages, message, options, commandService);
        this.confirmationChatService = confirmationChatService;
        setPermission(options.permissionMode);
        setDescription("Confirms or cancels an action.");
        setUsage("[confirm|cancel] [actionUuid]");
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if (!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }
        String action = args[0];
        UUID uuid = UUID.fromString(args[1]);

        if (action.equalsIgnoreCase("confirm")) {
            confirmationChatService.confirmAction(uuid, (Player) sender);
            return true;
        }
        if (action.equalsIgnoreCase("cancel")) {
            confirmationChatService.cancelAction(uuid, (Player) sender);
            return true;
        }
        return false;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 2;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.NONE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }
}
