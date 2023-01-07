package net.shortninja.staffplus.core.common.utils;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.load.InjectTubingPlugin;
import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
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
    private TubingBukkitPlugin tubingPlugin;

    public BukkitUtils(Messages messages, @InjectTubingPlugin TubingBukkitPlugin tubingPlugin) {
        this.messages = messages;
        this.tubingPlugin = tubingPlugin;
    }

    public static void sendEvent(Event event) {
        if (TubingBukkitPlugin.getPlugin().isEnabled()) {
            getScheduler().runTask(TubingBukkitPlugin.getPlugin(), () -> Bukkit.getPluginManager().callEvent(event));
        }
    }

    public static void sendEventOnThisTick(Event event) {
        if (TubingBukkitPlugin.getPlugin().isEnabled()) {
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    public static void sendEventAsync(Event event) {
        getScheduler().runTaskAsynchronously(TubingBukkitPlugin.getPlugin(), () -> Bukkit.getPluginManager().callEvent(event));
    }

    public void runTaskAsync(CommandSender sender, Runnable runnable) {
        getScheduler().runTaskAsynchronously(tubingPlugin, () -> {
            try {
                runnable.run();
            } catch (BusinessException e) {
                messages.send(sender, e.getMessage(), e.getPrefix());
            }
        });
    }

    public void runTaskLater(CommandSender sender, Runnable runnable) {
        getScheduler().runTaskLater(tubingPlugin, () -> {
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
