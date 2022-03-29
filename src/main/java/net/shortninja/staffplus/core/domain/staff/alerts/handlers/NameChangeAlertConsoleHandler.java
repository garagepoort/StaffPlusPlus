package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.namechanged.bungee.NameChangeBungeeDto;
import net.shortninja.staffplus.core.domain.player.namechanged.bungee.NameChangedBungeeEvent;
import net.shortninja.staffplus.core.domain.staff.alerts.config.AlertsConfiguration;
import net.shortninja.staffplusplus.chat.NameChangeEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

@IocListener(conditionalOnProperty = "alerts-module.name-notify-console=true")
public class NameChangeAlertConsoleHandler implements Listener {

    private final Messages messages;
    private final AlertsConfiguration alertsConfiguration;
    private final PermissionHandler permission;
    private final PlayerManager playerManager;

    public NameChangeAlertConsoleHandler(Messages messages, AlertsConfiguration alertsConfiguration, PermissionHandler permission, PlayerManager playerManager) {
        this.messages = messages;
        this.alertsConfiguration = alertsConfiguration;
        this.permission = permission;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void handle(NameChangeEvent nameChangeEvent) {
        if (permission.has(nameChangeEvent.getPlayer(), alertsConfiguration.permissionNameChangeBypass)) {
            return;
        }

        log(nameChangeEvent.getOldName(), nameChangeEvent.getServerName(), nameChangeEvent.getNewName());
    }

    @EventHandler
    public void handle(NameChangedBungeeEvent nameChangeEvent) {
        NameChangeBungeeDto nameChangeBungeeDto = nameChangeEvent.getNameChangeBungeeDto();
        Optional<SppPlayer> sppPlayer = playerManager.getOnOrOfflinePlayer(nameChangeBungeeDto.getPlayerUuid());
        if (sppPlayer.isPresent()) {
            if (permission.has(sppPlayer.get().getOfflinePlayer(), alertsConfiguration.permissionNameChangeBypass)) {
                return;
            }
            log(nameChangeBungeeDto.getOldName(), nameChangeBungeeDto.getServerName(), nameChangeBungeeDto.getNewName());
        }
    }

    private void log(String oldName, String serverName, String newName) {
        String message = messages.alertsName
            .replace("%old%", oldName)
            .replace("%server%", serverName)
            .replace("%new%", newName);
        StaffPlus.get().getLogger().info(message);
    }
}
