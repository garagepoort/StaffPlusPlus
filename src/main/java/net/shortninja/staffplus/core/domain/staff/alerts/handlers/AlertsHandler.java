package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.alerts.config.AlertsConfiguration;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import net.shortninja.staffplusplus.alerts.AlertType;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AlertsHandler {

    protected final AlertsConfiguration alertsConfiguration = IocContainer.get(Options.class).alertsConfiguration;
    protected final SessionManagerImpl sessionManager = IocContainer.get(SessionManagerImpl.class);
    protected final MessageCoordinator message = IocContainer.get(MessageCoordinator.class);
    protected final PermissionHandler permission = IocContainer.get(PermissionHandler.class);
    protected final Messages messages = IocContainer.get(Messages.class);

    public List<Player> getPlayersToNotify() {
        return sessionManager.getAll().stream()
            .filter(s -> s.getPlayer().isPresent() && s.shouldNotify(getType()) && permission.has(s.getPlayer().get(), getPermission()))
            .map(s -> s.getPlayer().get())
            .collect(Collectors.toList());
    }

    protected abstract AlertType getType();

    protected abstract String getPermission();
}
