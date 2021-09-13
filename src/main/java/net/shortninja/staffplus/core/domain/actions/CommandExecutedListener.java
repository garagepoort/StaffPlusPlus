package net.shortninja.staffplus.core.domain.actions;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean
@IocListener
public class CommandExecutedListener implements Listener {

    @EventHandler
    public void handleDelayedActions(CommandExecutedEvent event) {
        Bukkit.dispatchCommand(event.getExecutioner(), event.getCommand());
    }

}
