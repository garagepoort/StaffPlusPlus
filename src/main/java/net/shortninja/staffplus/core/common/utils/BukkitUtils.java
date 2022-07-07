package net.shortninja.staffplus.core.common.utils;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlusPlus;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import static org.bukkit.Bukkit.getScheduler;

@IocBean
public class BukkitUtils {

    private final Messages messages;

    public BukkitUtils(Messages messages) {
        this.messages = messages;
    }

    public static void sendEvent(Event event) {
        if (StaffPlusPlus.get().isEnabled()) {
            getScheduler().runTask(StaffPlusPlus.get(), () -> Bukkit.getPluginManager().callEvent(event));
        }
    }

    public static void sendEventOnThisTick(Event event) {
        if (StaffPlusPlus.get().isEnabled()) {
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    public static void sendEventAsync(Event event) {
        getScheduler().runTaskAsynchronously(StaffPlusPlus.get(), () -> Bukkit.getPluginManager().callEvent(event));
    }

    public void runTaskAsync(CommandSender sender, Runnable runnable) {
        getScheduler().runTaskAsynchronously(StaffPlusPlus.get(), () -> {
            try {
                runnable.run();
            } catch (BusinessException e) {
                messages.send(sender, e.getMessage(), e.getPrefix());
            }
        });
    }

    public void runTaskLater(CommandSender sender, Runnable runnable) {
        getScheduler().runTaskLater(StaffPlusPlus.get(), () -> {
            try {
                runnable.run();
            } catch (BusinessException e) {
                messages.send(sender, e.getMessage(), e.getPrefix());
            }
        }, 1);
    }

    public void runTaskLater(Runnable runnable) {
        this.runTaskLater(Bukkit.getConsoleSender(), runnable);
    }

    public void runTaskAsync(Runnable runnable) {
        this.runTaskAsync(Bukkit.getConsoleSender(), runnable);
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

    public static String getIpFromPlayer(Player player) {
        return player.getAddress().getAddress().getHostAddress().replace("/", "");
    }

}
