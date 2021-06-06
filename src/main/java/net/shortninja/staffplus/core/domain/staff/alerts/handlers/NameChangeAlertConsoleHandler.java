package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplusplus.chat.NameChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean(conditionalOnProperty = "alerts-module.name-notify-console=true")
@IocListener
public class NameChangeAlertConsoleHandler implements Listener {

    private final Messages messages;

    public NameChangeAlertConsoleHandler(Messages messages) {
        this.messages = messages;
    }

    @EventHandler
    public void handle(NameChangeEvent nameChangeEvent) {
        String message = messages.alertsName.replace("%old%", nameChangeEvent.getOldName()).replace("%new%", nameChangeEvent.getNewName());

        StaffPlus.get().getLogger().info(message);
    }

}
