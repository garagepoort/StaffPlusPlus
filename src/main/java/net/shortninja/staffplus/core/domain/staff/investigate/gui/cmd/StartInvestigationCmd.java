package net.shortninja.staffplus.core.domain.staff.investigate.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.cmd.*;
import net.shortninja.staffplus.core.common.gui.PagedSelector;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.confirmation.ChoiceChatService;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.investigation.InvestigationItemBuilder;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Command(
    command = "commands:commands.investigations.manage.start",
    permissions = "permissions:permissions.investigations.manage.investigate",
    description = "Start investigating a player",
    usage = "[player]"
)
@IocBean(conditionalOnProperty = "investigations-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class StartInvestigationCmd extends AbstractCmd {

    private final InvestigationService investigationService;
    private final PlayerManager playerManager;
    private final IProtocolService protocolService;
    private final InvestigationItemBuilder investigationItemBuilder;
    private final ChoiceChatService choiceChatService;
    private final Options options;

    public StartInvestigationCmd(Messages messages,
                                 CommandService commandService,
                                 PermissionHandler permissionHandler,
                                 InvestigationService investigationService,
                                 PlayerManager playerManager,
                                 IProtocolService protocolService,
                                 InvestigationItemBuilder investigationItemBuilder,
                                 ChoiceChatService choiceChatService, Options options) {
        super(messages, permissionHandler, commandService);
        this.investigationService = investigationService;
        this.playerManager = playerManager;
        this.protocolService = protocolService;
        this.investigationItemBuilder = investigationItemBuilder;
        this.choiceChatService = choiceChatService;
        this.options = options;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        validateIsPlayer(sender);

        if (args.length == 1) {
            Optional<Investigation> pausedInvestigation = investigationService.getPausedInvestigation((Player) sender, player);
            if (pausedInvestigation.isPresent()) {
                investigationService.resumeInvestigation((Player) sender, player);
                return true;
            }
            investigationService.startInvestigation((Player) sender, player);
        } else {
            List<Investigation> pausedInvestigations = investigationService.getPausedInvestigations((Player) sender);
            if (!pausedInvestigations.isEmpty()) {
                sendChoiceMessage((Player) sender);
            } else {
                investigationService.startInvestigation((Player) sender);
            }
        }

        return true;
    }

    private void sendChoiceMessage(Player sender) {
        choiceChatService.sendChoiceMessage(
            sender,
            messages.prefixInvestigations + "&6You currently have paused investigations.\n",
            "&aResume investigation",
            this::showInvestigationSelect,
            "&eStart new investigation",
            investigationService::startInvestigation);
    }

    private void showInvestigationSelect(Player sender) {
        new PagedSelector(sender, "Select investigation to resume", 0,
            selected -> {
                int investigationId = Integer.parseInt(protocolService.getVersionProtocol().getNbtString(selected));
                investigationService.resumeInvestigation(sender, investigationId);
            },
            (player1, target, offset, amount) -> investigationService.getPausedInvestigations(player1, offset, amount).stream()
                .map(investigationItemBuilder::build)
                .collect(Collectors.toList()))
            .show(sender);
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 0;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        if (options.investigationConfiguration.isAllowOfflineInvestigation()) {
            return PlayerRetrievalStrategy.OPTIONAL_BOTH;
        }
        return PlayerRetrievalStrategy.OPTIONAL_ONLINE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return Optional.empty();
        }
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
