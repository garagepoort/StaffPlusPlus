package net.shortninja.staffplus.core.domain.staff.investigate.actions;

import be.garagepoort.mcioc.configuration.ConfigObjectList;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.singletonList;

@IocBukkitListener
public class InvestigationActionsHook implements Listener {

    @ConfigProperty("investigations-module.start-investigation-commands")
    @ConfigObjectList(ConfiguredCommand.class)
    private List<ConfiguredCommand> startInvestigationCommands = new ArrayList<>();

    @ConfigProperty("investigations-module.conclude-investigation-commands")
    @ConfigObjectList(ConfiguredCommand.class)
    private List<ConfiguredCommand> concludeInvestigationCommands = new ArrayList<>();

    @ConfigProperty("investigations-module.pause-investigation-commands")
    @ConfigObjectList(ConfiguredCommand.class)
    private List<ConfiguredCommand> pauseInvestigationCommands = new ArrayList<>();

    private final ActionService actionService;
    private final PlayerManager playerManager;
    private final ConfiguredCommandMapper configuredCommandMapper;
    private final BukkitUtils bukkitUtils;

    public InvestigationActionsHook(ActionService actionService, PlayerManager playerManager, ConfiguredCommandMapper configuredCommandMapper, BukkitUtils bukkitUtils) {
        this.actionService = actionService;
        this.playerManager = playerManager;
        this.configuredCommandMapper = configuredCommandMapper;
        this.bukkitUtils = bukkitUtils;
    }

    @EventHandler
    public void onStart(InvestigationStartedEvent event) {
        executeActions(event.getInvestigation(), startInvestigationCommands);
    }

    @EventHandler
    public void onConclude(InvestigationConcludedEvent event) {
        executeActions(event.getInvestigation(), concludeInvestigationCommands);
    }

    @EventHandler
    public void onPause(InvestigationPausedEvent event) {
        executeActions(event.getInvestigation(), pauseInvestigationCommands);
    }

    private void executeActions(IInvestigation investigation, List<ConfiguredCommand> concludeInvestigationCommands) {
        Optional<SppPlayer> investigator = playerManager.getOnlinePlayer(investigation.getInvestigatorUuid());
        Optional<SppPlayer> investigated = investigation.getInvestigatedUuid().flatMap(playerManager::getOnlinePlayer);

        bukkitUtils.runTaskAsync(() -> {
            if (investigator.isPresent()) {
                Map<String, String> placeholders = new HashMap<>();
                investigator.ifPresent(sppPlayer -> placeholders.put("%investigator%", sppPlayer.getUsername()));
                investigated.ifPresent(sppPlayer -> placeholders.put("%investigated%", sppPlayer.getUsername()));

                Map<String, OfflinePlayer> targets = new HashMap<>();
                investigator.ifPresent(sppPlayer -> targets.put("investigator", sppPlayer.getOfflinePlayer()));
                investigated.ifPresent(sppPlayer -> targets.put("investigated", sppPlayer.getOfflinePlayer()));

                InvestigationInvestigatedActionFilter filter = new InvestigationInvestigatedActionFilter(investigation);
                actionService.createCommands(configuredCommandMapper.toCreateRequests(concludeInvestigationCommands, placeholders, targets, singletonList(filter)));
            }
        });
    }

}
