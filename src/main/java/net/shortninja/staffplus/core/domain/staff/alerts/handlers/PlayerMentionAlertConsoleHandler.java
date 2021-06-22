package net.shortninja.staffplus.core.domain.staff.alerts.handlers;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplusplus.chat.PlayerMentionedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean(conditionalOnProperty = "alerts-module.mention-notify-console=true")
@IocListener
public class PlayerMentionAlertConsoleHandler implements Listener {

    private final PermissionHandler permission;
    private final Options options;

    public PlayerMentionAlertConsoleHandler(PermissionHandler permission, Options options) {
        this.permission = permission;
        this.options = options;
    }

    @EventHandler
    public void handle(PlayerMentionedEvent event) {
        if (permission.has(event.getPlayer(), options.alertsConfiguration.getPermissionMentionBypass())) {
            return;
        }

        String message = "&7%target% &bhas mentioned %mentioned% in chat!".replace("%target%", event.getPlayer().getName()).replace("%mentioned%", event.getMentionedPlayer().getName());
        StaffPlus.get().getLogger().info(message);
    }

}
