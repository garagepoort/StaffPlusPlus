package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplusplus.chat.NameChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean(conditionalOnProperty = "alerts-module.name-notify-console=true")
@IocListener
public class NameChangeAlertConsoleHandler implements Listener {

    private final Messages messages;
    private final Options options;
    private final PermissionHandler permission;

    public NameChangeAlertConsoleHandler(Messages messages, Options options, PermissionHandler permission) {
        this.messages = messages;
        this.options = options;
        this.permission = permission;
    }

    @EventHandler
    public void handle(NameChangeEvent nameChangeEvent) {
        if (permission.has(nameChangeEvent.getPlayer(), options.alertsConfiguration.getPermissionNameChangeBypass())) {
            return;
        }

        String message = messages.alertsName.replace("%old%", nameChangeEvent.getOldName()).replace("%new%", nameChangeEvent.getNewName());
        StaffPlus.get().getLogger().info(message);
    }

}
