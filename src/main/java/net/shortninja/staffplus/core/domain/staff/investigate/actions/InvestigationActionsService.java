package net.shortninja.staffplus.core.domain.staff.investigate.actions;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.ConfiguredAction;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplusplus.investigate.IInvestigation;
import net.shortninja.staffplusplus.investigate.InvestigationConcludedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationPausedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationStartedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

@IocBean
@IocListener
public class InvestigationActionsService implements Listener {

    private final Options options;
    private final ActionService actionService;
    private final PlayerManager playerManager;

    public InvestigationActionsService(Options options, ActionService actionService, PlayerManager playerManager) {
        this.options = options;
        this.actionService = actionService;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onStart(InvestigationStartedEvent event) {
        executeActions(event.getInvestigation(), options.investigationConfiguration.getStartInvestigationActions());
    }

    @EventHandler
    public void onConclude(InvestigationConcludedEvent event) {
        executeActions(event.getInvestigation(), options.investigationConfiguration.getConcludeInvestigationCommands());
    }

    @EventHandler
    public void onPause(InvestigationPausedEvent event) {
        executeActions(event.getInvestigation(), options.investigationConfiguration.getPauseInvestigationCommands());
    }

    private void executeActions(IInvestigation investigation, List<ConfiguredAction> concludeInvestigationCommands) {
        Optional<SppPlayer> investigator = playerManager.getOnlinePlayer(investigation.getInvestigatorUuid());
        Optional<SppPlayer> investigated = playerManager.getOnlinePlayer(investigation.getInvestigatedUuid());

        if (investigator.isPresent() && investigated.isPresent()) {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%investigator%", investigator.get().getUsername());
            placeholders.put("%investigated%", investigated.get().getUsername());
            actionService.executeActions(new InvestigationActionTargetProvider(investigator.get(), investigated.get()), concludeInvestigationCommands, Collections.emptyList(), placeholders);
        }
    }

}
