package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.StaffPlus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.List;
import java.util.stream.Collectors;

public class TabComplete implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTabComplete(TabCompleteEvent e) {
        if (StaffPlus.get().options.vanishEnabled && !StaffPlus.get().options.vanishSuggestionsEnabled
<<<<<<< HEAD
                && StaffPlus.get().vanishHandler.isVanished((Player)e.getSender())) {
=======
                && StaffPlus.get().vanishHandler.isVanished((Player) e.getSender())) {
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
            final List<String> modified = e.getCompletions().stream().filter(s -> StaffPlus.get().vanishHandler.getVanished()
                    .stream()
                    .map(Player::getName)
                    .anyMatch(x -> x.equalsIgnoreCase(s))).collect(Collectors.toList());

            e.setCompletions(modified);
        }
    }

}
