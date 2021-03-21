package net.shortninja.staffplus.domain.staff.alerts.handlers;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.config.Messages;
import net.shortninja.staffplus.common.utils.MessageCoordinator;
import net.shortninja.staffplus.common.utils.PermissionHandler;
import net.shortninja.staffplus.domain.staff.alerts.config.AlertsConfiguration;
import net.shortninja.staffplus.session.SessionManagerImpl;
import net.shortninja.staffplusplus.alerts.AlertType;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AlertsHandler {

    protected final AlertsConfiguration alertsConfiguration = IocContainer.getOptions().alertsConfiguration;
    protected final SessionManagerImpl sessionManager = IocContainer.getSessionManager();
    protected final MessageCoordinator message = IocContainer.getMessage();
    protected final PermissionHandler permission = IocContainer.getPermissionHandler();
    protected final Messages messages = IocContainer.getMessages();

    public List<Player> getPlayersToNotify() {
        return sessionManager.getAll().stream()
            .filter(s -> s.getPlayer().isPresent() && s.shouldNotify(getType()) && permission.has(s.getPlayer().get(), getPermission()))
            .map(s -> s.getPlayer().get())
            .collect(Collectors.toList());
    }

    protected abstract AlertType getType();

    protected abstract String getPermission();
}
