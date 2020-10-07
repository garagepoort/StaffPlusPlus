package net.shortninja.staffplus.staff.ban;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Optional;

public class BanListener implements Listener {
    private final BanService banService = IocContainer.getBanService();

    public BanListener() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        Optional<Ban> ban = banService.getBan(player.getUniqueId());
        if (ban.isPresent()) {
            if (ban.get().getEndDate() == null) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "[Banned] You are permanently banned from this server");
            } else {
                long differenceInMinutes = JavaUtils.getDuration(ban.get().getEndDate());
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "[Banned] You are temporarily banned from this server. Ban ends in " + differenceInMinutes + " minutes." );
            }
        }
    }
}