package net.shortninja.staffplus.core.domain.player.ip.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplusplus.ips.IpHistoryClearedEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean
@IocListener
public class IpsChatNotifier implements Listener {

    @ConfigProperty("permissions:ips.notifications")
    private String staffNotificationPermission;

    private final Messages messages;

    public IpsChatNotifier(Messages messages) {
        this.messages = messages;
    }

    @EventHandler
    public void historyClearedEvent(IpHistoryClearedEvent event) {
        CommandSender issuer = event.getIssuer();
        messages.send(issuer, messages.ipsHistoryCleared.replace("%player%", event.getTarget().getUsername()), messages.ipsPrefix);
        messages.sendGroupMessage(messages.ipsHistoryClearedNotification
            .replace("%issuer%", getIssuerName(event.getIssuer()))
            .replace("%player%", event.getTarget().getUsername()), staffNotificationPermission, messages.ipsPrefix);
    }

    private String getIssuerName(CommandSender sender) {
        return sender instanceof Player ? sender.getName() : "CONSOLE";
    }
}
