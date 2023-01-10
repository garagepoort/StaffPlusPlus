package net.shortninja.staffplus.core.examine;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.BOTH;

@Command(
    command = "commands:examine",
    permissions = "permissions:examine",
    description = "Examines the player's inventory",
    usage = "[player]",
    playerRetrievalStrategy = BOTH
)
@IocBean(conditionalOnProperty = "examine-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class ExamineCmd extends AbstractCmd {
    private final ExamineService examineService;
    private final PlayerManager playerManager;

    public ExamineCmd(Messages messages,
                      CommandService commandService,
                      PlayerManager playerManager,
                      PermissionHandler permissionHandler, ExamineService examineService) {
        super(messages, permissionHandler, commandService);
        this.examineService = examineService;
        this.playerManager = playerManager;
    }


    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer, Map<String, String> optionalParameters) {
        if (!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }
        if (((Player) sender).getUniqueId() == targetPlayer.getId()) {
            throw new BusinessException("Cannot examine yourself");
        }

        examineService.examine((Player) sender, targetPlayer);
        return true;
    }


    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 1;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.ofNullable(args[0]);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}