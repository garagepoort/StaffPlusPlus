package net.shortninja.staffplus.common.actions;

import net.shortninja.staffplus.common.actions.database.ActionableRepository;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.staff.delayedactions.DelayedActionsRepository;
import net.shortninja.staffplus.unordered.Actionable;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ActionService {

    private final DelayedActionsRepository delayedActionsRepository;
    private final ActionableRepository actionableRepository;
    private final PlayerManager playerManager;
    private final ActionExecutioner actionExecutioner;

    public ActionService(DelayedActionsRepository delayedActionsRepository, ActionableRepository actionableRepository, PlayerManager playerManager, ActionExecutioner actionExecutioner) {
        this.delayedActionsRepository = delayedActionsRepository;
        this.actionableRepository = actionableRepository;
        this.playerManager = playerManager;
        this.actionExecutioner = actionExecutioner;
    }

    public List<ConfiguredAction> executeActions(Actionable actionable, SppPlayer target, List<ConfiguredAction> actions, ActionFilter actionFilter) {
        List<ConfiguredAction> executedCommands = new ArrayList<>();
        for (ConfiguredAction action : actions) {
            if (actionExecutioner.executeAction(actionable, target, action, actionFilter)) {
                executedCommands.add(action);
            }
        }
        return executedCommands;
    }

    public List<ConfiguredAction> executeActions(Actionable actionable, SppPlayer target, List<ConfiguredAction> actions) {
        return this.executeActions(actionable, target, actions, null);
    }

    public List<ExecutableActionEntity> rollbackActionable(Actionable actionable) {
        Optional<SppPlayer> target = playerManager.getOnOrOfflinePlayer(actionable.getTargetUuid());
        if (!target.isPresent()) {
            return null;
        }

        List<ExecutableActionEntity> actions = actionableRepository.getActions(actionable)
            .stream().filter(a -> a.isExecuted() && !a.isRollbacked() && StringUtils.isNotEmpty(a.getRollbackCommand()))
            .collect(Collectors.toList());
        return this.rollbackActions(actions, target.get());
    }

    public List<ExecutableActionEntity> getActions(Actionable actionable) {
        return actionableRepository.getActions(actionable);
    }

    public void markExecuted(int executableActionId) {
        actionableRepository.markExecuted(executableActionId);
    }

    public void markRollbacked(int executableActionId) {
        actionableRepository.markRollbacked(executableActionId);
    }

    private List<ExecutableActionEntity> rollbackActions(List<ExecutableActionEntity> actions, SppPlayer target) {
        List<ExecutableActionEntity> executedCommands = new ArrayList<>();
        for (ExecutableActionEntity action : actions) {
            if (actionExecutioner.rollbackAction(action, target)) {
                executedCommands.add(action);
            }
        }
        return executedCommands;
    }
}
