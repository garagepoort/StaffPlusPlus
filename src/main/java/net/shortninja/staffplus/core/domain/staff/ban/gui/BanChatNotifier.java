package net.shortninja.staffplus.core.domain.staff.ban.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplusplus.ban.BanEvent;
import net.shortninja.staffplusplus.ban.IBan;
import net.shortninja.staffplusplus.ban.UnbanEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.shortninja.staffplus.core.domain.staff.ban.BanMessageStringUtil.replaceBanPlaceholders;

@IocBean
@IocListener
public class BanChatNotifier implements Listener {

    private final Messages messages;
    private final Options options;

    public BanChatNotifier(Messages messages, Options options) {
        this.messages = messages;
        this.options = options;
    }

    @EventHandler
    public void notifyPlayerBanned(BanEvent event) {
        IBan ban = event.getBan();
        String banMessage = ban.getEndDate() == null ? messages.permanentBanned : messages.tempBanned;
        String message = replaceBanPlaceholders(banMessage, ban.getTargetName(), ban.getIssuerName(), ban.getReason(), ban.getEndTimestamp());

        this.messages.sendGroupMessage(message, options.banConfiguration.getStaffNotificationPermission(), messages.prefixGeneral);
    }

    @EventHandler
    public void notifyUnban(UnbanEvent event) {
        IBan ban = event.getBan();
        String unbanMessage = replaceBanPlaceholders(messages.unbanned, ban.getTargetName(), ban.getUnbannedByName(), ban.getReason(), ban.getEndTimestamp());

        this.messages.sendGroupMessage(unbanMessage, options.banConfiguration.getStaffNotificationPermission(), messages.prefixGeneral);
    }

}
