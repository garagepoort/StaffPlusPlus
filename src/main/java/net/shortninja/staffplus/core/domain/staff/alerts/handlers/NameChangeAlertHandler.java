package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.alerts.config.AlertsConfiguration;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.chat.NameChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean
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
        for (Player player : getPlayersToNotify()) {
            messages.send(player, messages.alertsName.replace("%old%", nameChangeEvent.getOldName()).replace("%new%", nameChangeEvent.getNewName()), messages.prefixGeneral, getPermission());
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
