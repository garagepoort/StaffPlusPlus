package net.shortninja.staffplus.core.domain.staff.mute.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplusplus.mute.IMute;
import net.shortninja.staffplusplus.mute.MuteEvent;
import net.shortninja.staffplusplus.mute.UnmuteEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
            String message = messages.permanentMuted
                .replace("%target%", mute.getTargetName())
                .replace("%reason%", mute.getReason())
                .replace("%issuer%", mute.getIssuerName());
            messages.sendGroupMessage(message, options.muteConfiguration.getStaffNotificationPermission(), messages.prefixGeneral);
        } else {
            String message = messages.tempMuted
                .replace("%target%", mute.getTargetName())
                .replace("%reason%", mute.getReason())
                .replace("%issuer%", mute.getIssuerName())
                .replace("%duration%", mute.getHumanReadableDuration());
            messages.sendGroupMessage(message, options.muteConfiguration.getStaffNotificationPermission(), messages.prefixGeneral);
        }
    }

    @EventHandler
    public void notifyUnmute(UnmuteEvent event) {
        IMute mute = event.getMute();
        String unmuteMessage = messages.unmuted
            .replace("%target%", mute.getTargetName())
            .replace("%issuer%", mute.getUnmutedByName());
        messages.sendGroupMessage(unmuteMessage, options.muteConfiguration.getStaffNotificationPermission(), messages.prefixGeneral);
    }

}
