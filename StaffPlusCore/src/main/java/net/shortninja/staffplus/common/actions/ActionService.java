package net.shortninja.staffplus.common.actions;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.staff.delayedactions.DelayedActionsRepository;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

import static net.shortninja.staffplus.common.actions.ActionRunStrategy.*;

public class ActionService {

    private DelayedActionsRepository delayedActionsRepository;

    public ActionService(DelayedActionsRepository delayedActionsRepository) {
        this.delayedActionsRepository = delayedActionsRepository;
    }

    public boolean executeAction(CommandSender sender, SppPlayer target, ExecutableAction action, ActionFilter actionFilter) {
        if (actionFilter != null && !actionFilter.isValidAction(sender, target, action)) {
            return false;
        }
        if (action.getRunStrategy() == ALWAYS
            || (action.getRunStrategy() == ONLINE && target.isOnline())
            || (action.getRunStrategy() == DELAY && target.isOnline())) {

            Bukkit.getScheduler().runTask(StaffPlus.get(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action.getCommand().replace("%player%", target.getUsername())));
            return true;
        } else if (action.getRunStrategy() == DELAY && !target.isOnline()) {
            delayedActionsRepository.saveDelayedAction(target.getId(), action.getCommand());
            return true;
        }
        return false;
    }

    public List<ExecutableAction> executeActions(CommandSender sender, SppPlayer target, List<ExecutableAction> actions, ActionFilter actionFilter) {
        List<ExecutableAction> executedCommands = new ArrayList<>();
        for (ExecutableAction action : actions) {
            if (executeAction(sender, target, action, actionFilter)) {
                executedCommands.add(action);
            }
        }
        return executedCommands;
    }

    public List<ExecutableAction> executeActions(CommandSender sender, SppPlayer target, List<ExecutableAction> actions) {
        return this.executeActions(sender, target, actions, null);
    }
}
