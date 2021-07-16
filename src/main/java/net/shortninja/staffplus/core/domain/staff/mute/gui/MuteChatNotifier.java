package net.shortninja.staffplus.core.domain.staff.mute.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.domain.staff.mute.config.MuteConfiguration;
import net.shortninja.staffplusplus.mute.IMute;
import net.shortninja.staffplusplus.mute.MuteEvent;
import net.shortninja.staffplusplus.mute.UnmuteEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.shortninja.staffplus.core.domain.staff.mute.MuteMessageStringUtil.replaceMutePlaceholders;

@IocBean
@IocListener
public class MuteChatNotifier implements Listener {

    private final Messages messages;
    private final MuteConfiguration muteConfiguration;

    public MuteChatNotifier(Messages messages, MuteConfiguration muteConfiguration) {
        this.messages = messages;
        this.muteConfiguration = muteConfiguration;
    }

    @EventHandler
    public void notifyPlayerMuted(MuteEvent event) {
        IMute mute = event.getMute();
        if (mute.getEndTimestamp() == null) {
            String message = replaceMutePlaceholders(messages.permanentMuted, mute);
            messages.sendGroupMessage(message, muteConfiguration.staffNotificationPermission, messages.prefixGeneral);
        } else {
            String message = replaceMutePlaceholders(messages.tempMuted, mute);
            messages.sendGroupMessage(message, muteConfiguration.staffNotificationPermission, messages.prefixGeneral);
        }
    }

    @EventHandler
    public void notifyUnmute(UnmuteEvent event) {
        IMute mute = event.getMute();
        String unmuteMessage = replaceMutePlaceholders(messages.unmuted, mute);
        messages.sendGroupMessage(unmuteMessage, muteConfiguration.staffNotificationPermission, messages.prefixGeneral);
    }

}
