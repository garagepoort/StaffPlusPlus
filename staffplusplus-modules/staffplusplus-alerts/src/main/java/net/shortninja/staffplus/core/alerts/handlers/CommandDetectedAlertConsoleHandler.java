package net.shortninja.staffplus.core.alerts.handlers;

import be.garagepoort.mcioc.load.InjectTubingPlugin;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplusplus.commanddetection.CommandDetectedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBukkitListener(conditionalOnProperty = "alerts-module.command-detection-console=true")
public class CommandDetectedAlertConsoleHandler implements Listener {

    private final Messages messages;
    private final TubingBukkitPlugin staffPlusPlus;

    public CommandDetectedAlertConsoleHandler(Messages messages, @InjectTubingPlugin TubingBukkitPlugin staffPlusPlus) {
        this.messages = messages;
        this.staffPlusPlus = staffPlusPlus;
    }

    @EventHandler
    public void handle(CommandDetectedEvent commandDetectedEvent) {
        String message = messages.alertsCommandDetected
            .replace("%target%", commandDetectedEvent.getPlayer().getName())
            .replace("%command%", commandDetectedEvent.getCommand());

        staffPlusPlus.getLogger().info(message);
    }
}
