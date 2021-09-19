package net.shortninja.staffplus.core.domain.staff.mute.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mute.config.MuteConfiguration;
import net.shortninja.staffplusplus.mute.IMute;
import net.shortninja.staffplusplus.mute.MuteEvent;
import net.shortninja.staffplusplus.mute.MuteExtensionEvent;
import net.shortninja.staffplusplus.mute.MuteReductionEvent;
import net.shortninja.staffplusplus.mute.UnmuteEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.shortninja.staffplus.core.domain.staff.mute.MuteMessageStringUtil.replaceMutePlaceholders;

@IocBean
@IocListener
public class MuteChatNotifier implements Listener {

    private final Messages messages;
    private final MuteConfiguration muteConfiguration;
    private final PlayerManager playerManager;

    public MuteChatNotifier(Messages messages, MuteConfiguration muteConfiguration, PlayerManager playerManager) {
        this.messages = messages;
        this.muteConfiguration = muteConfiguration;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onMuteEvent(MuteEvent event) {
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

    @EventHandler
    public void notifyMuteExtension(MuteExtensionEvent event) {
        IMute mute = event.getMute();
        String message = replaceMutePlaceholders(messages.muteExtended, mute).replace("%extensionDuration%", JavaUtils.toHumanReadableDuration(event.getExtensionDuration()));
        messages.send(event.getExecutor(), message, messages.prefixGeneral);
    }

    @EventHandler
    public void notifyMuteReduction(MuteReductionEvent event) {
        IMute mute = event.getMute();
        String message = replaceMutePlaceholders(messages.muteReduced, mute).replace("%reductionDuration%", JavaUtils.toHumanReadableDuration(event.getReductionDuration()));
        messages.send(event.getExecutor(), message, messages.prefixGeneral);
    }


    public void notifyPlayerMuted(IMute mute, Player player) {
        if (!mute.isSoftMute()) {
            String message = replaceMutePlaceholders(messages.muted, mute);
            this.messages.send(player, message, messages.prefixGeneral);
        }
    }
}
