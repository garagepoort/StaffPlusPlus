package net.shortninja.staffplus.staff.warn;

import me.rayzr522.jsonmessage.JSONMessage;
import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.stream.Collectors;

public class WarnListener implements Listener {

    private final WarnService warnService = IocContainer.getWarnService();
    private final Options options = IocContainer.getOptions();
    private final Permission permission = IocContainer.getPermissionHandler();

    public WarnListener() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void notifyWarnings(PlayerJoinEvent event) {
        if (!options.warningConfiguration.isNotifyUser()) {
            return;
        }
        List<Warning> warnings = warnService.getWarnings(event.getPlayer().getUniqueId());
        if(options.warningConfiguration.isAlwaysNotifyUser()) {
            if(!warnings.isEmpty()) {
                sendMessage(event, warnings);
            }
        } else{
            List<Warning> unreadWarnings = warnings.stream().filter(w -> !w.isRead()).collect(Collectors.toList());
            if (!unreadWarnings.isEmpty()) {
                sendMessage(event, unreadWarnings);
            }
        }
    }

    private void sendMessage(PlayerJoinEvent event, List<Warning> unreadWarnings) {
        JSONMessage message = JSONMessage.create("You have " + unreadWarnings.size() + " new warnings")
            .color(ChatColor.GOLD);

        if (permission.has(event.getPlayer(), options.warningConfiguration.getMyWarningsPermission())) {
            message.then(" View your warnings!")
                .color(ChatColor.BLUE)
                .tooltip("Click to view your warnings")
                .runCommand("/" + options.warningConfiguration.getMyWarningsCmd());
        }

        message.send(event.getPlayer());
    }
}
