package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.alerts.config.AlertsConfiguration;
import net.shortninja.staffplusplus.commanddetection.CommandDetectedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean(conditionalOnProperty = "alerts-module.command-detection-console=true")
@IocListener
public class CommandDetectedAlertConsoleHandler implements Listener {

    private final Messages messages;
    private final PermissionHandler permissionHandler;
    private final AlertsConfiguration alertsConfiguration;

    public CommandDetectedAlertConsoleHandler(Messages messages, PermissionHandler permissionHandler, AlertsConfiguration alertsConfiguration) {
        this.messages = messages;
        this.permissionHandler = permissionHandler;
        this.alertsConfiguration = alertsConfiguration;
    }

    @EventHandler
    public void handle(CommandDetectedEvent commandDetectedEvent) {
        if (permissionHandler.has(commandDetectedEvent.getPlayer(), alertsConfiguration.permissionCommandDetectionBypass)) {
            return;
        }
        String message = messages.alertsCommandDetected
            .replace("%target%", commandDetectedEvent.getPlayer().getName())
            .replace("%command%", commandDetectedEvent.getCommand());

        StaffPlus.get().getLogger().info(message);
    }
}
