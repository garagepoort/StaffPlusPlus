package net.shortninja.staffplus.core.domain.staff.ban;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.config.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Optional;

import static net.shortninja.staffplus.core.domain.staff.ban.BanMessageStringUtil.replaceBanPlaceholders;

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
    public void onJoin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        Optional<Ban> optionalBan = banService.getBanByBannedUuid(player.getUniqueId());
        if (optionalBan.isPresent()) {
            Ban ban = optionalBan.get();
            if (ban.getEndTimestamp() == null) {
                String banMessage = replaceBanPlaceholders(messages.permanentBannedKick, ban.getTargetName(), ban.getIssuerName(), ban.getReason(), ban.getEndTimestamp());
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, messages.colorize(banMessage));
            } else {
                String banMessage = replaceBanPlaceholders(messages.tempBannedKick, ban.getTargetName(), ban.getIssuerName(), ban.getReason(), ban.getEndTimestamp());
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, messages.colorize(banMessage));
            }
        }
    }
}