package net.shortninja.staffplus.domain.staff.ban;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Optional;

public class BanListener implements Listener {
    private final BanService banService = IocContainer.getBanService();
    private final Messages messages = IocContainer.getMessages();

    public BanListener() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
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