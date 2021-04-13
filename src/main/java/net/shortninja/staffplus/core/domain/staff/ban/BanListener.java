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

        Optional<Ban> ban = banService.getBanByBannedUuid(player.getUniqueId());
        if (ban.isPresent()) {
            if (ban.get().getEndTimestamp() == null) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, messages.permanentBannedKick
                    .replace("%reason%", ban.get().getReason()));
            } else {
                String message = messages.tempBannedKick
                    .replace("%duration%", ban.get().getHumanReadableDuration())
                        .replace("%reason%", ban.get().getReason());
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, message);
            }
        }
    }
}