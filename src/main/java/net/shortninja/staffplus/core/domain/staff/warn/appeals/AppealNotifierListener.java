package net.shortninja.staffplus.core.domain.staff.warn.appeals;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import me.rayzr522.jsonmessage.JSONMessage;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.StaffPlusPlusJoinedEvent;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.config.AppealConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.database.AppealRepository;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.ManageWarningsConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@IocBean(conditionalOnProperty = "warnings-module.appeals.enabled=true")
@IocListener
public class AppealNotifierListener implements Listener {

    private final AppealRepository appealRepository;
    private final ManageWarningsConfiguration manageWarningsConfiguration;
    private final AppealConfiguration appealConfiguration;
    private final PermissionHandler permission;
    private final Messages messages;
    private final BukkitUtils bukkitUtils;

    public AppealNotifierListener(AppealRepository appealRepository, ManageWarningsConfiguration manageWarningsConfiguration, AppealConfiguration appealConfiguration, PermissionHandler permission, Messages messages, BukkitUtils bukkitUtils) {
        this.appealRepository = appealRepository;
        this.manageWarningsConfiguration = manageWarningsConfiguration;
        this.appealConfiguration = appealConfiguration;
        this.permission = permission;
        this.messages = messages;
        this.bukkitUtils = bukkitUtils;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void notifyAppeals(StaffPlusPlusJoinedEvent event) {
        if (!permission.has(event.getPlayer(), appealConfiguration.permissionNotifications)) {
            return;
        }

        bukkitUtils.runTaskAsync(() -> {
            int openAppeals = appealRepository.getCountOpenAppeals();
            if (openAppeals > 0) {
                sendMessage(event, openAppeals);
            }
        });
    }

    private void sendMessage(StaffPlusPlusJoinedEvent event, int appealsCount) {
        JSONMessage message = JavaUtils.buildClickableMessage(
            messages.openAppealsNotify.replace("%appealsCount%", String.valueOf(appealsCount)),
            "View unresolved appeals!",
            "Click to view unresolved appeals",
            manageWarningsConfiguration.commandManageAppealedWarningsGui,
            canManageAppeal(event));
        message.send(event.getPlayer());
    }

    private boolean canManageAppeal(StaffPlusPlusJoinedEvent event) {
        return permission.has(event.getPlayer(), appealConfiguration.approveAppealPermission)
            || permission.has(event.getPlayer(), appealConfiguration.rejectAppealPermission);
    }
}
