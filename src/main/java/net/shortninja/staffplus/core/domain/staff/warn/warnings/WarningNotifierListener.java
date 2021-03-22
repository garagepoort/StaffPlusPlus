package net.shortninja.staffplus.core.domain.staff.warn.warnings;

import me.rayzr522.jsonmessage.JSONMessage;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getScheduler;

public class WarningNotifierListener implements Listener {

    private final WarnService warnService = IocContainer.get(WarnService.class);
    private final Options options = IocContainer.get(Options.class);
    private final PermissionHandler permission = IocContainer.get(PermissionHandler.class);
    private final Messages messages = IocContainer.get(Messages.class);

    public WarningNotifierListener() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void notifyWarnings(PlayerJoinEvent event) {
        if (!options.warningConfiguration.isNotifyUser()) {
            return;
        }

        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            List<Warning> warnings = warnService.getWarnings(event.getPlayer().getUniqueId(), false);
            if (options.warningConfiguration.isAlwaysNotifyUser()) {
                if (!warnings.isEmpty()) {
                    sendMessage(event, warnings);
                }
            } else {
                List<Warning> unreadWarnings = warnings.stream().filter(w -> !w.isRead()).collect(Collectors.toList());
                if (!unreadWarnings.isEmpty()) {
                    sendMessage(event, unreadWarnings);
                }
            }
        });
    }

    private void sendMessage(PlayerJoinEvent event, List<Warning> unreadWarnings) {
        String notifyMessage = messages.warningsNotify.replace("%warningsCount%", String.valueOf(unreadWarnings.size()));
        JSONMessage message = JavaUtils.buildClickableMessage(
            notifyMessage,
            "View your warnings!",
            "Click to view your warnings",
            options.warningConfiguration.getMyWarningsCmd(),
            permission.has(event.getPlayer(), options.warningConfiguration.getMyWarningsPermission()));
        message.send(event.getPlayer());
    }
}
