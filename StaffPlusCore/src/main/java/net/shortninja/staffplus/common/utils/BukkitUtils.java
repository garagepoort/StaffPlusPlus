package net.shortninja.staffplus.common.utils;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

import static org.bukkit.Bukkit.getScheduler;

public class BukkitUtils {
    public static void sendEvent(Event event) {
        getScheduler().runTask(StaffPlus.get(), () -> {
            Bukkit.getPluginManager().callEvent(event);
        });
    }

    public static int getInventorySize(int amountOfItems) {
        int division = amountOfItems / 9;
        int rest = amountOfItems % 9;
        if (rest != 0) {
            division++;
            return division * 9;
        }
        return amountOfItems;
    }

    public static void runTaskAsync(CommandSender sender, Runnable runnable) {
        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            try {
                runnable.run();
            } catch (BusinessException e) {
                sender.sendMessage(e.getMessage());
            }
        });
    }
}
