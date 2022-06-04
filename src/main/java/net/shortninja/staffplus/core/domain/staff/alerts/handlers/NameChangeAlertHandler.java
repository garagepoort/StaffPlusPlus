package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.player.namechanged.bungee.NameChangeBungeeDto;
import net.shortninja.staffplus.core.domain.player.namechanged.bungee.NameChangedBungeeEvent;
import net.shortninja.staffplus.core.domain.staff.alerts.config.AlertsConfiguration;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.chat.NameChangeEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

@IocListener
public class NameChangeAlertHandler extends AlertsHandler implements Listener {

    private final AlertsConfiguration alertsConfiguration;

    public NameChangeAlertHandler(AlertsConfiguration alertsConfiguration,
                                  PlayerSettingsRepository playerSettingsRepository,
                                  OnlineSessionsManager sessionManager,
                                  PermissionHandler permission,
                                  Messages messages,
                                  PlayerManager playerManager) {
        super(alertsConfiguration, playerSettingsRepository, sessionManager, permission, messages, playerManager);
        this.alertsConfiguration = alertsConfiguration;
    }

    @EventHandler
    public void handle(NameChangeEvent nameChangeEvent) {
        if (!alertsConfiguration.alertsNameNotify) {
            return;
        }
        if (permission.has(nameChangeEvent.getPlayer(), alertsConfiguration.permissionNameChangeBypass)) {
            return;
        }
        notifyPlayers(nameChangeEvent.getOldName(), nameChangeEvent.getNewName(), nameChangeEvent.getServerName());
    }

    @EventHandler
    public void handle(NameChangedBungeeEvent nameChangeEvent) {
        if (!alertsConfiguration.alertsNameNotify) {
            return;
        }
        NameChangeBungeeDto nameChangeBungeeDto = nameChangeEvent.getNameChangeBungeeDto();

        Optional<SppPlayer> sppPlayer = playerManager.getOnOrOfflinePlayer(nameChangeBungeeDto.getPlayerUuid());
        if (sppPlayer.isPresent() && permission.has(sppPlayer.get().getOfflinePlayer(), alertsConfiguration.permissionNameChangeBypass)) {
            return;
        }
        notifyPlayers(nameChangeBungeeDto.getOldName(), nameChangeBungeeDto.getNewName(), nameChangeBungeeDto.getServerName());
    }

    private void notifyPlayers(String oldName, String newName, String serverName) {
        for (Player player : getPlayersToNotify()) {
            messages.send(player, messages.alertsName
                .replace("%old%", oldName)
                .replace("%server%", serverName)
                .replace("%new%", newName), messages.prefixGeneral, getPermission());
        }
    }

    @Override
    protected AlertType getType() {
        return AlertType.NAME_CHANGE;
    }

    @Override
    protected String getPermission() {
        return alertsConfiguration.permissionNameChange;
    }
}
