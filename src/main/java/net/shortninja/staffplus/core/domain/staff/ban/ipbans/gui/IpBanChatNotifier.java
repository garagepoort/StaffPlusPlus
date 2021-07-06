package net.shortninja.staffplus.core.domain.staff.ban.ipbans.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBanConfiguration;
import net.shortninja.staffplusplus.ban.IIpBan;
import net.shortninja.staffplusplus.ban.IpBanEvent;
import net.shortninja.staffplusplus.ban.IpUnbanEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBanMessageStringUtil.replaceBanPlaceholders;

@IocBean
@IocListener
public class IpBanChatNotifier implements Listener {

    private final Messages messages;
    private final IpBanConfiguration banConfiguration;
    private final PlayerManager playerManager;

    public IpBanChatNotifier(Messages messages, IpBanConfiguration banConfiguration, PlayerManager playerManager) {
        this.messages = messages;
        this.banConfiguration = banConfiguration;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void notifyIpBanned(IpBanEvent event) {
        IIpBan ban = event.getBan();

        playerManager.getOnlinePlayer(ban.getIssuerUuid()).ifPresent(p -> messages.send(p.getPlayer(), "&6Ban executed", messages.prefixBans));

        if (!ban.isSilentBan()) {
            String banMessage = !ban.getEndTimestamp().isPresent() ? messages.ipbanPermabanned : messages.ipbanTempbanned;
            String message = replaceBanPlaceholders(banMessage, ban);
            this.messages.sendGroupMessage(message, banConfiguration.staffNotificationPermission, messages.prefixBans);
        }
    }

    @EventHandler
    public void notifyUnban(IpUnbanEvent event) {
        IIpBan ban = event.getBan();

        playerManager.getOnlinePlayer(ban.getIssuerUuid()).ifPresent(p -> messages.send(p.getPlayer(), "&6Unban executed", messages.prefixBans));

        if (!ban.isSilentUnban()) {
            String unbanMessage = replaceBanPlaceholders(messages.ipbanUnbanned, ban);
            this.messages.sendGroupMessage(unbanMessage, banConfiguration.staffNotificationPermission, messages.prefixBans);
        }
    }

}
