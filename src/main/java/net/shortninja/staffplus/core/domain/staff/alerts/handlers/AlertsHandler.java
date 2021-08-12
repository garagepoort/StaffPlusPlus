package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.alerts.config.AlertsConfiguration;
import net.shortninja.staffplusplus.alerts.AlertType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AlertsHandler {

    protected final AlertsConfiguration alertsConfiguration;
    protected final PlayerSettingsRepository playerSettingsRepository;
    protected final OnlineSessionsManager sessionManager;
    protected final PermissionHandler permission;
    protected final Messages messages;
    protected final PlayerManager playerManager;

    protected AlertsHandler(AlertsConfiguration alertsConfiguration, PlayerSettingsRepository playerSettingsRepository, OnlineSessionsManager sessionManager, PermissionHandler permission, Messages messages, PlayerManager playerManager) {
        this.alertsConfiguration = alertsConfiguration;
        this.playerSettingsRepository = playerSettingsRepository;
        this.sessionManager = sessionManager;
        this.permission = permission;
        this.messages = messages;
        this.playerManager = playerManager;
    }

    public List<Player> getPlayersToNotify() {
        return Bukkit.getOnlinePlayers().stream()
            .filter(s -> shouldNotify(playerSettingsRepository.get(s), getType()))
            .filter(p -> permission.has(p.getPlayer(), getPermission()))
            .collect(Collectors.toList());
    }


    public boolean shouldNotify(PlayerSettings s, AlertType alertType) {
        return s.getAlertOptions().contains(alertType);
    }

    protected abstract AlertType getType();

    protected abstract String getPermission();
}
