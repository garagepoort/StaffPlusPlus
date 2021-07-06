package net.shortninja.staffplus.core.domain.staff.ban.ipbans;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Messages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.List;

import static net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBanMessageStringUtil.replaceBanPlaceholders;


@IocBean
@IocListener
public class IpBanPreventJoinListener implements Listener {
    private final IpBanService banService;
    private final Messages messages;

    public IpBanPreventJoinListener(IpBanService banService, Messages messages) {
        this.banService = banService;
        this.messages = messages;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        String ipAddress = event.getAddress().getHostAddress().replace("/", "");

        List<IpBan> ipBans = banService.findMatchingIpBans(ipAddress);
        if (!ipBans.isEmpty()) {
            IpBan ban = ipBans.get(0);
            if (!ban.getEndTimestamp().isPresent()) {
                String banMessage = replaceBanPlaceholders(messages.ipbanPermabannedKick, ban);
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, messages.colorize(banMessage));
            } else {
                String banMessage = replaceBanPlaceholders(messages.ipbanTempbannedKick, ban);
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, messages.colorize(banMessage));
            }
        }
    }
}