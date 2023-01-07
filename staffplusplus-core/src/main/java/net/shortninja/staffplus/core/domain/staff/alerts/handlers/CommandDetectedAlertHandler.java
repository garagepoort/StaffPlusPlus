package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.alerts.config.AlertsConfiguration;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.commanddetection.CommandDetectedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

@IocBukkitListener(conditionalOnProperty = "alerts-module.command-detection=true")
public class CommandDetectedAlertHandler extends AlertsHandler implements Listener {

    private final Options options;

    public CommandDetectedAlertHandler(AlertsConfiguration alertsConfiguration,
                                       OnlineSessionsManager sessionManager,
                                       PlayerSettingsRepository playerSettingsRepository,
                                       PermissionHandler permission,
                                       Messages messages,
                                       PlayerManager playerManager, Options options) {
        super(alertsConfiguration, playerSettingsRepository, sessionManager, permission, messages, playerManager);
        this.options = options;
    }

    @EventHandler
    public void handle(CommandDetectedEvent commandDetectedEvent) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(commandDetectedEvent.getTimestamp()),
            TimeZone.getDefault().toZoneId());
        String timestamp = localDateTime.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ofPattern(options.timestampFormat));

        for (Player player : getPlayersToNotify()) {

            messages.send(player, messages.alertsCommandDetected
                .replace("%target%", commandDetectedEvent.getPlayer().getName())
                .replace("%world%", commandDetectedEvent.getWorld().getName())
                .replace("%timestamp%", timestamp)
                .replace("%command%", commandDetectedEvent.getCommand()), messages.prefixGeneral, getPermission());
        }
    }

    @Override
    protected AlertType getType() {
        return AlertType.COMMAND_DETECTION;
    }

    @Override
    protected String getPermission() {
        return alertsConfiguration.permissionCommandDetection;
    }
}
