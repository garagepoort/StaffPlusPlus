package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplusplus.commanddetection.CommandDetectedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBukkitListener(conditionalOnProperty = "alerts-module.command-detection-console=true")
public class CommandDetectedAlertConsoleHandler implements Listener {

    private final Messages messages;

    public CommandDetectedAlertConsoleHandler(Messages messages) {
        this.messages = messages;
    }

    @EventHandler
    public void handle(CommandDetectedEvent commandDetectedEvent) {
        String message = messages.alertsCommandDetected
            .replace("%target%", commandDetectedEvent.getPlayer().getName())
            .replace("%command%", commandDetectedEvent.getCommand());

        StaffPlus.get().getLogger().info(message);
    }
}
