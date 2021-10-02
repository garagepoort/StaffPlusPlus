package net.shortninja.staffplus.core.domain.staff.kick.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplusplus.kick.KickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean
@IocListener
public class KickChatNotifier implements Listener {

    private final Messages messages;
    @ConfigProperty("permissions:kick-notifications")
    public String kickNotificationPermission;

    public KickChatNotifier(Messages messages) {
        this.messages = messages;
    }

    @EventHandler
    public void onKick(KickEvent kickEvent) {
        String message = messages.kickedNotify
            .replace("%target%", kickEvent.getKick().getTargetName())
            .replace("%issuer%", kickEvent.getKick().getIssuerName())
            .replace("%reason%", kickEvent.getKick().getReason());
        this.messages.sendGroupMessage(message, kickNotificationPermission, messages.prefixGeneral);
    }
}
