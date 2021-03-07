package net.shortninja.staffplus.common.actions;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.actions.database.ActionableRepository;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.staff.delayedactions.DelayedActionsRepository;
import net.shortninja.staffplusplus.Actionable;
import org.bukkit.Bukkit;

import java.util.List;

import static net.shortninja.staffplus.common.actions.ActionRunStrategy.*;

public class ActionExecutioner {


    private final ActionableRepository actionableRepository;
    private final DelayedActionsRepository delayedActionsRepository;

    public ActionExecutioner(ActionableRepository actionableRepository, DelayedActionsRepository delayedActionsRepository) {
        this.actionableRepository = actionableRepository;
        this.delayedActionsRepository = delayedActionsRepository;
    }

    boolean executeAction(Actionable actionable, SppPlayer target, ConfiguredAction action, List<ActionFilter> actionFilters) {
        if (actionFilters != null && actionFilters.stream().anyMatch(a -> !a.isValidAction(target, action))) {
            return false;
        }
        if (runActionNow(target, action.getRunStrategy())) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action.getCommand().replace("%player%", target.getUsername()));
            ExecutableActionEntity executableActionEntity = new ExecutableActionEntity(action, actionable, true);
            actionableRepository.saveActionable(executableActionEntity);
            return true;
        } else if (action.getRunStrategy() == DELAY && !target.isOnline()) {
            ExecutableActionEntity executableActionEntity = new ExecutableActionEntity(action, actionable, false);
            int executableActionId = actionableRepository.saveActionable(executableActionEntity);
            delayedActionsRepository.saveDelayedAction(target.getId(), action.getCommand(), executableActionId, false);
            return true;
        }
        return false;
    }

    boolean executeAction(SppPlayer target, ConfiguredAction action, List<ActionFilter> actionFilters) {
        if (actionFilters != null && actionFilters.stream().anyMatch(a -> !a.isValidAction(target, action))) {
            return false;
        }
        if (runActionNow(target, action.getRunStrategy())) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action.getCommand().replace("%player%", target.getUsername()));
            return true;
        } else if (action.getRunStrategy() == DELAY && !target.isOnline()) {
            delayedActionsRepository.saveDelayedAction(target.getId(), action.getCommand());
            return true;
        }
        return false;
    }

    boolean rollbackAction(ExecutableActionEntity action, SppPlayer target) {
        if (runActionNow(target, action.getRollbackRunStrategy())) {
            Bukkit.getScheduler().runTask(StaffPlus.get(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action.getRollbackCommand().replace("%player%", target.getUsername())));
            actionableRepository.markRollbacked(action.getId());
            return true;
        } else if (action.getRollbackRunStrategy() == DELAY && !target.isOnline()) {
            delayedActionsRepository.saveDelayedAction(target.getId(), action.getRollbackCommand(), action.getId(), true);
            return true;
        }
        return false;
    }

    private boolean runActionNow(SppPlayer target, ActionRunStrategy runStrategy) {
        return runStrategy == ALWAYS
            || (runStrategy == ONLINE && target.isOnline())
            || (runStrategy == DELAY && target.isOnline());
    }
}
