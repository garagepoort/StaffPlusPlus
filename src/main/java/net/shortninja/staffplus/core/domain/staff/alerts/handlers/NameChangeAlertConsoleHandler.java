package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.alerts.config.AlertsConfiguration;
import net.shortninja.staffplusplus.chat.NameChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean(conditionalOnProperty = "alerts-module.name-notify-console=true")
@IocListener
public class NameChangeAlertConsoleHandler implements Listener {

    private final Messages messages;
    private final AlertsConfiguration alertsConfiguration;
    private final PermissionHandler permission;

    public NameChangeAlertConsoleHandler(Messages messages, AlertsConfiguration alertsConfiguration, PermissionHandler permission) {
        this.messages = messages;
        this.alertsConfiguration = alertsConfiguration;
        this.permission = permission;
    }

    @EventHandler
    public void handle(NameChangeEvent nameChangeEvent) {
        if (permission.has(nameChangeEvent.getPlayer(), alertsConfiguration.permissionNameChangeBypass)) {
            return;
        }

        String message = messages.alertsName.replace("%old%", nameChangeEvent.getOldName()).replace("%new%", nameChangeEvent.getNewName());
        StaffPlus.get().getLogger().info(message);
    }

}
