package net.shortninja.staffplus.core.domain.staff.investigate.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.confirmation.ConfirmationChatService;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@IocBean(conditionalOnProperty = "investigations-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class StartInvestigationCmd extends AbstractCmd {

    private final InvestigationService investigationService;
    private final ConfirmationChatService confirmationChatService;
    private final PlayerManager playerManager;

    public StartInvestigationCmd(Messages messages, Options options, CommandService commandService, InvestigationService investigationService, ConfirmationChatService confirmationChatService, PlayerManager playerManager) {
        super(options.investigationConfiguration.getStartInvestigationCmd(), messages, options, commandService);
        this.investigationService = investigationService;
        this.confirmationChatService = confirmationChatService;
        this.playerManager = playerManager;
        setDescription("Start investigating a player");
        setUsage("[playername]");
        setPermission(options.investigationConfiguration.getInvestigatePermission());
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if (!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }

        if (investigationService.getPausedInvestigation(player).isPresent()) {
            confirmationChatService.sendConfirmationMessage((Player) sender, messages.prefixInvestigations + " &6This player currently has an investigation paused. Do you want to resume this investigation?",
                p -> investigationService.resumeInvestigation((Player) sender, player),
                p -> messages.send(sender, "&6Action cancelled", messages.prefixInvestigations));
            return true;
        }

        investigationService.startInvestigation((Player) sender, player);
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 1;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        if (options.investigationConfiguration.isAllowOfflineInvestigation()) {
            return PlayerRetrievalStrategy.BOTH;
        }
        return PlayerRetrievalStrategy.ONLINE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.of(args[0]);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (options.investigationConfiguration.isAllowOfflineInvestigation() && args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }
        return super.tabComplete(sender, alias, args);
    }
}
