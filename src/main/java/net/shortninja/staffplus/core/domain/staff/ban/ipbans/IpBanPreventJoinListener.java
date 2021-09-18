package net.shortninja.staffplus.core.domain.staff.ban.ipbans;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.List;


@IocBean
@IocListener
public class IpBanPreventJoinListener implements Listener {
    private final IpBanService banService;
    private final Messages messages;
    private final IpBanTemplateResolver ipBanTemplateResolver;

    public IpBanPreventJoinListener(IpBanService banService, Messages messages, IpBanTemplateResolver ipBanTemplateResolver) {
        this.banService = banService;
        this.messages = messages;
        this.ipBanTemplateResolver = ipBanTemplateResolver;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        String ipAddress = event.getAddress().getHostAddress().replace("/", "");

        List<IpBan> ipBans = banService.findMatchingIpBans(ipAddress);
        if (!ipBans.isEmpty()) {
            IpBan ban = ipBans.get(0);
            BanType banType = !ban.getEndTimestamp().isPresent() ? BanType.PERM_BAN : BanType.TEMP_BAN;

            String templateMessage;
            if (ban.getTemplate().isPresent() && ipBanTemplateResolver.hasTemplate(ban.getTemplate().get())) {
                templateMessage = ipBanTemplateResolver.resolveTemplate(ban.getTemplate().get(), banType);
            } else {
                templateMessage = ipBanTemplateResolver.resolveTemplate(null, banType);
            }

            String banMessage = IpBanMessageStringUtil.replaceBanPlaceholders(templateMessage, ban);
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, messages.colorize(banMessage));
        }
    }
}