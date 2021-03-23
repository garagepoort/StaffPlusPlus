package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.chat.NameChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean
public class NameChangeAlertHandler extends AlertsHandler implements Listener {

    public NameChangeAlertHandler(Options options, SessionManagerImpl sessionManager, MessageCoordinator message, PermissionHandler permission, Messages messages) {
        super(options, sessionManager, message, permission, messages);
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler
    public void handle(NameChangeEvent nameChangeEvent) {
        if (!alertsConfiguration.isNameNotifyEnabled()) {
            return;
        }

        for (Player player : getPlayersToNotify()) {
            message.send(player, messages.alertsName.replace("%old%", nameChangeEvent.getOldName()).replace("%new%", nameChangeEvent.getNewName()), messages.prefixGeneral, getPermission());
        }
    }

    @Override
    protected AlertType getType() {
        return AlertType.NAME_CHANGE;
    }

    @Override
    protected String getPermission() {
        return alertsConfiguration.getPermissionNameChange();
    }
}
