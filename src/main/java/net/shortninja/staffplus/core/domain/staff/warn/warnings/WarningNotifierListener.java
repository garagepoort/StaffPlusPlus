package net.shortninja.staffplus.core.domain.staff.warn.warnings;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.staffplusplus.craftbukkit.common.json.rayzr.JSONMessage;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.JsonSenderService;
import net.shortninja.staffplus.core.common.StaffPlusPlusJoinedEvent;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getScheduler;

@IocBean
public class WarningNotifierListener implements Listener {

    private final WarnService warnService;
    private final PermissionHandler permission;
    private final Messages messages;
    private final WarningConfiguration warningConfiguration;
    private final JsonSenderService jsonSenderService;

    public WarningNotifierListener(WarnService warnService,
                                   PermissionHandler permission,
                                   Messages messages,
                                   WarningConfiguration warningConfiguration, JsonSenderService jsonSenderService) {
        this.warnService = warnService;
        this.permission = permission;
        this.messages = messages;
        this.warningConfiguration = warningConfiguration;
        this.jsonSenderService = jsonSenderService;
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void notifyWarnings(StaffPlusPlusJoinedEvent event) {
        if (!warningConfiguration.isNotifyUser()) {
            return;
        }

        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            List<Warning> warnings = warnService.getWarnings(event.getPlayer().getUniqueId(), false);
            if (warningConfiguration.isAlwaysNotifyUser()) {
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

    private void sendMessage(StaffPlusPlusJoinedEvent event, List<Warning> unreadWarnings) {
        String notifyMessage = messages.warningsNotify.replace("%warningsCount%", String.valueOf(unreadWarnings.size()));
        JSONMessage message = JavaUtils.buildClickableMessage(
            notifyMessage,
            "View your warnings!",
            "Click to view your warnings",
            warningConfiguration.getMyWarningsCmd(),
            permission.has(event.getPlayer(), warningConfiguration.getMyWarningsPermission()));
        jsonSenderService.send(message, event.getPlayer());
    }
}
