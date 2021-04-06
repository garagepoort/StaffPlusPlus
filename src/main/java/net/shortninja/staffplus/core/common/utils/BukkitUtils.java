package net.shortninja.staffplus.core.common.utils;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

import static org.bukkit.Bukkit.getScheduler;

@IocBean
public class BukkitUtils {

    private final Messages messages;

    public BukkitUtils(Messages messages) {
        this.messages = messages;
    }

    public static void sendEvent(Event event) {
        getScheduler().runTask(StaffPlus.get(), () -> Bukkit.getPluginManager().callEvent(event));
    }

    public static void sendEventAsync(Event event) {
        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> Bukkit.getPluginManager().callEvent(event));
    }

    public void runTaskAsync(CommandSender sender, Runnable runnable) {
        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            try {
                runnable.run();
            } catch (BusinessException e) {
                messages.send(sender, e.getMessage(), e.getPrefix());
            }
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

}
