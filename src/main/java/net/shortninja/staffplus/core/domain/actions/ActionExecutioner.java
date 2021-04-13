package net.shortninja.staffplus.core.domain.actions;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.domain.actions.database.ActionableRepository;
import net.shortninja.staffplus.core.domain.delayedactions.database.DelayedActionsRepository;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.Actionable;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.shortninja.staffplus.core.domain.actions.ActionRunStrategy.*;
import static net.shortninja.staffplus.core.domain.delayedactions.Executor.CONSOLE;

@IocBean
public class ActionExecutioner {


    private final ActionableRepository actionableRepository;
    private final DelayedActionsRepository delayedActionsRepository;

    public ActionExecutioner(ActionableRepository actionableRepository, DelayedActionsRepository delayedActionsRepository) {
        this.actionableRepository = actionableRepository;
        this.delayedActionsRepository = delayedActionsRepository;
    }

    boolean executeAction(Actionable actionable, ActionTargetProvider targetProvider, ConfiguredAction action, List<ActionFilter> actionFilters) {
        Optional<SppPlayer> target = targetProvider.getTarget(action);
        if(!target.isPresent()) {
            return false;
        }

        if (actionFilters != null && actionFilters.stream().anyMatch(a -> !a.isValidAction(target.get(), action))) {
            return false;
        }
        if (runActionNow(target.get(), action.getRunStrategy())) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action.getCommand().replace("%player%", target.get().getUsername()));
            ExecutableActionEntity executableActionEntity = new ExecutableActionEntity(action, actionable, true);
            actionableRepository.saveActionable(executableActionEntity);
            return true;
        } else if (action.getRunStrategy() == DELAY && !target.get().isOnline()) {
            ExecutableActionEntity executableActionEntity = new ExecutableActionEntity(action, actionable, false);
            int executableActionId = actionableRepository.saveActionable(executableActionEntity);
            delayedActionsRepository.saveDelayedAction(target.get().getId(), action.getCommand(), CONSOLE, executableActionId, false);
            return true;
        }
        return false;
    }

    boolean executeAction(ActionTargetProvider targetProvider, ConfiguredAction action, List<ActionFilter> actionFilters, Map<String, String> placeholders) {
        Optional<SppPlayer> target = targetProvider.getTarget(action);
        if(!target.isPresent()) {
            return false;
        }
        placeholders.putIfAbsent("%player%", target.get().getUsername());

        if (actionFilters != null && actionFilters.stream().anyMatch(a -> !a.isValidAction(target.get(), action))) {
            return false;
        }
        String commandLine = replacePlaceholders(action.getCommand(), placeholders);

        if (runActionNow(target.get(), action.getRunStrategy())) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandLine);
            return true;
        } else if (action.getRunStrategy() == DELAY && !target.get().isOnline()) {
            delayedActionsRepository.saveDelayedAction(target.get().getId(), commandLine, CONSOLE);
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
            delayedActionsRepository.saveDelayedAction(target.getId(), action.getRollbackCommand(), CONSOLE, action.getId(), true);
            return true;
        }
        return false;
    }

    private String replacePlaceholders(String message, Map<String, String> placeholders) {
        String result = message;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        return result;
    }

    private boolean runActionNow(SppPlayer target, ActionRunStrategy runStrategy) {
        return runStrategy == ALWAYS
            || (runStrategy == ONLINE && target.isOnline())
            || (runStrategy == DELAY && target.isOnline());
    }
}
