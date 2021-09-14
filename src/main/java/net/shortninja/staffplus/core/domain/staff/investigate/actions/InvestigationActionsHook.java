package net.shortninja.staffplus.core.domain.staff.investigate.actions;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.ConfiguredCommand;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommandMapper;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.investigate.IInvestigation;
import net.shortninja.staffplusplus.investigate.InvestigationConcludedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationPausedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationStartedEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@IocBean
@IocListener
public class InvestigationActionsHook implements Listener {

    private final Options options;
    private final ActionService actionService;
    private final PlayerManager playerManager;
    private final ConfiguredCommandMapper configuredCommandMapper;

    public InvestigationActionsHook(Options options, ActionService actionService, PlayerManager playerManager, ConfiguredCommandMapper configuredCommandMapper) {
        this.options = options;
        this.actionService = actionService;
        this.playerManager = playerManager;
        this.configuredCommandMapper = configuredCommandMapper;
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

    private void executeActions(IInvestigation investigation, List<ConfiguredCommand> concludeInvestigationCommands) {
        Optional<SppPlayer> investigator = playerManager.getOnlinePlayer(investigation.getInvestigatorUuid());
        Optional<SppPlayer> investigated = investigation.getInvestigatedUuid().flatMap(playerManager::getOnlinePlayer);

        if (investigator.isPresent()) {
            Map<String, String> placeholders = new HashMap<>();
            investigator.ifPresent(sppPlayer -> placeholders.put("%investigator%", sppPlayer.getUsername()));
            investigated.ifPresent(sppPlayer -> placeholders.put("%investigated%", sppPlayer.getUsername()));

            Map<String, OfflinePlayer> targets = new HashMap<>();
            investigator.ifPresent(sppPlayer -> targets.put("investigator", sppPlayer.getOfflinePlayer()));
            investigated.ifPresent(sppPlayer -> targets.put("investigated", sppPlayer.getOfflinePlayer()));

            actionService.createCommands(configuredCommandMapper.toCreateRequests(concludeInvestigationCommands, placeholders, targets, Collections.emptyList()));
        }
    }

}
