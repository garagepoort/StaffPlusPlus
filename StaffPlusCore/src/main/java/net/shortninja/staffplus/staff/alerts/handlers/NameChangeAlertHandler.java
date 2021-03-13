package net.shortninja.staffplus.staff.alerts.handlers;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.chat.NameChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NameChangeAlertHandler extends AlertsHandler implements Listener {

    public NameChangeAlertHandler() {
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
