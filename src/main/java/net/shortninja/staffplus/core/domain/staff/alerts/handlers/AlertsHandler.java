package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.alerts.config.AlertsConfiguration;
import net.shortninja.staffplusplus.alerts.AlertType;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AlertsHandler {

    protected final AlertsConfiguration alertsConfiguration;
    protected final SessionManagerImpl sessionManager;
    protected final PermissionHandler permission;
    protected final Messages messages;

    protected AlertsHandler(Options options, SessionManagerImpl sessionManager, PermissionHandler permission, Messages messages) {
        this.alertsConfiguration = options.alertsConfiguration;
        this.sessionManager = sessionManager;

        this.permission = permission;
        this.messages = messages;
    }

    public List<Player> getPlayersToNotify() {
        return sessionManager.getAll().stream()
            .filter(s -> s.getPlayer().isPresent() && s.shouldNotify(getType()) && permission.has(s.getPlayer().get(), getPermission()))
            .map(s -> s.getPlayer().get())
            .collect(Collectors.toList());
    }

    protected abstract AlertType getType();

    protected abstract String getPermission();
}
