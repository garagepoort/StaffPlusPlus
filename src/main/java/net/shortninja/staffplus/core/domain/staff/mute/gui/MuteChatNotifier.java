package net.shortninja.staffplus.core.domain.staff.mute.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
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
    private final Options options;

    public MuteChatNotifier(Messages messages, Options options) {
        this.messages = messages;
        this.options = options;
    }

    @EventHandler
    public void notifyPlayerMuted(MuteEvent event) {
        IMute mute = event.getMute();
        if (mute.getEndTimestamp() == null) {
            String message = replaceMutePlaceholders(messages.permanentMuted, mute);
            messages.sendGroupMessage(message, options.muteConfiguration.getStaffNotificationPermission(), messages.prefixGeneral);
        } else {
            String message = replaceMutePlaceholders(messages.tempMuted, mute);
            messages.sendGroupMessage(message, options.muteConfiguration.getStaffNotificationPermission(), messages.prefixGeneral);
        }
    }

    @EventHandler
    public void notifyUnmute(UnmuteEvent event) {
        IMute mute = event.getMute();
        String unmuteMessage = replaceMutePlaceholders(messages.unmuted, mute);
        messages.sendGroupMessage(unmuteMessage, options.muteConfiguration.getStaffNotificationPermission(), messages.prefixGeneral);
    }

}
