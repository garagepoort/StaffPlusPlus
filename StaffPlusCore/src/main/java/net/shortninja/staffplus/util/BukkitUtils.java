package net.shortninja.staffplus.util;

import net.shortninja.staffplus.StaffPlus;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import static org.bukkit.Bukkit.getScheduler;

public class BukkitUtils {
    public static void sendEvent(Event event) {
        getScheduler().runTask(StaffPlus.get(), () -> {
            Bukkit.getPluginManager().callEvent(event);
        });
    }
}
