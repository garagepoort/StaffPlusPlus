package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;

import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.chat.PlayerMentionedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean
public class PlayerMentionAlertHandler extends AlertsHandler implements Listener {

    public PlayerMentionAlertHandler(Options options, SessionManagerImpl sessionManager, PermissionHandler permission, Messages messages) {
        super(options, sessionManager, permission, messages);
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler
    public void handle(PlayerMentionedEvent event) {
        if (!alertsConfiguration.isMentionNotifyEnabled()) {
            return;
        }

        if (event.getMentionedPlayer().isOnline()) {
            PlayerSession session = sessionManager.get(event.getMentionedPlayer().getUniqueId());
            if (session.getPlayer().isPresent()) {
                Player mentionedPlayer = session.getPlayer().get();
                if (session.shouldNotify(AlertType.MENTION) && permission.has(mentionedPlayer, alertsConfiguration.getPermissionMention())) {
                    messages.send(mentionedPlayer, messages.alertsMention.replace("%target%", event.getPlayer().getName()), messages.prefixGeneral, alertsConfiguration.getPermissionMention());
                    alertsConfiguration.getAlertsSound().ifPresent(s -> s.play(mentionedPlayer));
                }
            }
        }

    }

    @Override
    protected AlertType getType() {
        return AlertType.MENTION;
    }

    @Override
    protected String getPermission() {
        return alertsConfiguration.getPermissionMention();
    }
}
