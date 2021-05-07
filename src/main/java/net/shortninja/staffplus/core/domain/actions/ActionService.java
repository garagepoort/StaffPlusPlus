package net.shortninja.staffplus.core.domain.actions;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.domain.actions.database.ActionableRepository;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.Actionable;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@IocBean
public class ActionService {

    private final ActionableRepository actionableRepository;
    private final PlayerManager playerManager;
    private final ActionExecutioner actionExecutioner;

    public ActionService(ActionableRepository actionableRepository, PlayerManager playerManager, ActionExecutioner actionExecutioner) {
        this.actionableRepository = actionableRepository;
        this.playerManager = playerManager;
        this.actionExecutioner = actionExecutioner;
    }

    public List<ConfiguredAction> executeActions(Actionable actionable, ActionTargetProvider targetProvider, List<ConfiguredAction> actions, List<ActionFilter> actionFilters) {
        return actions.stream()
            .filter(action -> actionExecutioner.executeAction(actionable, targetProvider, action, actionFilters))
            .collect(Collectors.toList());
    }

    public void executeActions(ActionTargetProvider targetProvider, List<ConfiguredAction> actions, List<ActionFilter> actionFilters, Map<String, String> placeholders) {
        actions.forEach(action -> actionExecutioner.executeAction(targetProvider, action, actionFilters, placeholders));
    }

    public List<ConfiguredAction> executeActions(Actionable actionable, ActionTargetProvider targetProvider, List<ConfiguredAction> actions) {
        return this.executeActions(actionable, targetProvider, actions, null);
    }

    public void rollbackActionable(Actionable actionable) {
        Optional<SppPlayer> target = playerManager.getOnOrOfflinePlayer(actionable.getTargetUuid());
        if (!target.isPresent()) {
            return;
        }

        List<ExecutableActionEntity> actions = getRollbackActions(actionable);
        actions.forEach(action -> actionExecutioner.rollbackAction(action, target.get()));
    }

    public List<ExecutableActionEntity> getRollbackActions(Actionable actionable) {
        return actionableRepository.getActions(actionable)
            .stream().filter(a -> a.isExecuted() && !a.isRollbacked() && StringUtils.isNotEmpty(a.getRollbackCommand()))
            .collect(Collectors.toList());
    }

    public void markExecuted(int executableActionId) {
        actionableRepository.markExecuted(executableActionId);
    }

    public void markRollbacked(int executableActionId) {
        actionableRepository.markRollbacked(executableActionId);
    }

    public void deleteActions(Actionable actionable) {
        actionableRepository.delete(actionable);
    }
}
