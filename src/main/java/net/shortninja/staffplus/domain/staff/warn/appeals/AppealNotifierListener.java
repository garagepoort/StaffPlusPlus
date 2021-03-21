package net.shortninja.staffplus.domain.staff.warn.appeals;

import me.rayzr522.jsonmessage.JSONMessage;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.JavaUtils;
import net.shortninja.staffplus.common.config.Messages;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.common.utils.PermissionHandler;
import net.shortninja.staffplus.domain.staff.warn.appeals.database.AppealRepository;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static org.bukkit.Bukkit.getScheduler;

public class AppealNotifierListener implements Listener {

    private final AppealRepository appealRepository = IocContainer.getAppealRepository();
    private final Options options = IocContainer.getOptions();
    private final PermissionHandler permission = IocContainer.getPermissionHandler();
    private final Messages messages = IocContainer.getMessages();

    public AppealNotifierListener() {
        if (options.appealConfiguration.isEnabled()) {
            Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void notifyAppeals(PlayerJoinEvent event) {
        if (!permission.has(event.getPlayer(), options.appealConfiguration.getPermissionNotifications())) {
            return;
        }


        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            int openAppeals = appealRepository.getCountOpenAppeals();
            if (openAppeals > 0) {
                sendMessage(event, openAppeals);
            }
        });
    }

    private void sendMessage(PlayerJoinEvent event, int appealsCount) {
        JSONMessage message = JavaUtils.buildClickableMessage(
            messages.openAppealsNotify.replace("%appealsCount%", String.valueOf(appealsCount)),
            "View unresolved appeals!",
            "Click to view unresolved appeals",
            options.manageWarningsConfiguration.getCommandManageAppealedWarningsGui(),
            canManageAppeal(event));
        message.send(event.getPlayer());
    }

    private boolean canManageAppeal(PlayerJoinEvent event) {
        return permission.has(event.getPlayer(), options.appealConfiguration.getApproveAppealPermission())
            || permission.has(event.getPlayer(), options.appealConfiguration.getRejectAppealPermission());
    }
}
