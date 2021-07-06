package net.shortninja.staffplus.core.domain.staff.ban.playerbans;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Messages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.Optional;

import static net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanMessageStringUtil.replaceBanPlaceholders;

@IocBean
@IocListener
public class BanListener implements Listener {
    private final BanService banService;
    private final Messages messages;

    public BanListener(BanService banService, Messages messages) {
        this.banService = banService;
        this.messages = messages;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        Optional<Ban> optionalBan = banService.getBanByBannedUuid(event.getUniqueId());
        if (optionalBan.isPresent()) {
            Ban ban = optionalBan.get();
            if (ban.getEndTimestamp() == null) {
                String banMessage = replaceBanPlaceholders(messages.permanentBannedKick, ban);
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, messages.colorize(banMessage));
            } else {
                String banMessage = replaceBanPlaceholders(messages.tempBannedKick, ban);
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, messages.colorize(banMessage));
            }
        }
    }
}