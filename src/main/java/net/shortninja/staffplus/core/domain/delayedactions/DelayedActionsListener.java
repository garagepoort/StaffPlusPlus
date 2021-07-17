package net.shortninja.staffplus.core.domain.delayedactions;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean
@IocListener
public class DelayedActionsListener implements Listener {

    private final ActionService actionService;

    public DelayedActionsListener(ActionService actionService) {
        this.actionService = actionService;
    }

    @EventHandler
    public void handleDelayedActions(DelayedActionsExecutedEvent event) {
        event.getDelayedActions().forEach(delayedAction -> {
            CommandSender sender = delayedAction.getExecutor() == Executor.CONSOLE ? Bukkit.getConsoleSender() : event.getPlayer();
            Bukkit.dispatchCommand(sender, delayedAction.getCommand().replace("%player%", event.getPlayer().getName()));
            updateActionable(delayedAction);
        });
    }

    private void updateActionable(DelayedAction delayedAction) {
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            if (delayedAction.getExecutableActionId().isPresent()) {
                if (delayedAction.isRollback()) {
                    actionService.markRollbacked(delayedAction.getExecutableActionId().get());
                } else {
                    actionService.markExecuted(delayedAction.getExecutableActionId().get());
                }
            }
        });
    }
}
