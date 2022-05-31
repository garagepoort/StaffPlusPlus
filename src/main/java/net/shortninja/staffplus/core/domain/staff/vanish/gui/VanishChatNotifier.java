package net.shortninja.staffplus.core.domain.staff.vanish.gui;

import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplusplus.vanish.VanishOffEvent;
import net.shortninja.staffplusplus.vanish.VanishOnEvent;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocListener
public class VanishChatNotifier implements Listener {

    private final Messages messages;

    public VanishChatNotifier(Messages messages) {
        this.messages = messages;
    }

    @EventHandler
    public void onVanish(VanishOnEvent vanishOnEvent) {
        String message = getMessage(vanishOnEvent.getType())
            .replace("%status%", messages.enabled);
        if (StringUtils.isNotEmpty(message)) {
            this.messages.send(vanishOnEvent.getPlayer(), message, messages.prefixGeneral);
        }
    }

    @EventHandler
    public void onUnVanish(VanishOffEvent vanishOffEvent) {
        String message = getMessage(vanishOffEvent.getType())
            .replace("%status%", messages.disabled);
        if (StringUtils.isNotEmpty(message)) {
            this.messages.send(vanishOffEvent.getPlayer(), message, messages.prefixGeneral);
        }
    }

    private String getMessage(VanishType vanishType) {
        switch (vanishType) {
            case LIST:
                return messages.listVanish;
            case PLAYER:
                return messages.playerVanish;
            case TOTAL:
                return messages.totalVanish;
            default:
                return "";
        }
    }
}
