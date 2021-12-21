package net.shortninja.staffplus.core.domain.staff.warn.appeals;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import me.rayzr522.jsonmessage.JSONMessage;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.StaffPlusPlusJoinedEvent;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.staff.appeals.database.AppealRepository;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.ManageWarningsConfiguration;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfiguration;
import net.shortninja.staffplusplus.appeals.AppealableType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@IocBean(conditionalOnProperty = "warnings-module.appeals.enabled=true")
@IocListener
public class WarningAppealNotifierListener implements Listener {

    private final AppealRepository appealRepository;
    private final ManageWarningsConfiguration manageWarningsConfiguration;
    private final WarningAppealConfiguration warningAppealConfiguration;
    private final PermissionHandler permission;
    private final Messages messages;
    private final BukkitUtils bukkitUtils;
    private final ServerSyncConfiguration serverSyncConfiguration;

    public WarningAppealNotifierListener(AppealRepository appealRepository, ManageWarningsConfiguration manageWarningsConfiguration, WarningAppealConfiguration warningAppealConfiguration, PermissionHandler permission, Messages messages, BukkitUtils bukkitUtils, ServerSyncConfiguration serverSyncConfiguration) {
        this.appealRepository = appealRepository;
        this.manageWarningsConfiguration = manageWarningsConfiguration;
        this.warningAppealConfiguration = warningAppealConfiguration;
        this.permission = permission;
        this.messages = messages;
        this.bukkitUtils = bukkitUtils;
        this.serverSyncConfiguration = serverSyncConfiguration;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void notifyAppeals(StaffPlusPlusJoinedEvent event) {
        if (!permission.has(event.getPlayer(), warningAppealConfiguration.permissionNotifications)) {
            return;
        }

        bukkitUtils.runTaskAsync(() -> {
            int openAppeals = appealRepository.getCountOpenAppeals(AppealableType.WARNING, "sp_warnings", serverSyncConfiguration.warningSyncServers);
            if (openAppeals > 0) {
                sendMessage(event, openAppeals);
            }
        });
    }

    private void sendMessage(StaffPlusPlusJoinedEvent event, int appealsCount) {
        JSONMessage message = JavaUtils.buildClickableMessage(
            messages.warningOpenAppealsNotify.replace("%appealsCount%", String.valueOf(appealsCount)),
            "View unresolved appeals!",
            "Click to view unresolved appeals",
            manageWarningsConfiguration.commandManageAppealedWarningsGui,
            canManageAppeal(event));
        message.send(event.getPlayer());
    }

    private boolean canManageAppeal(StaffPlusPlusJoinedEvent event) {
        return permission.has(event.getPlayer(), warningAppealConfiguration.approveAppealPermission)
            || permission.has(event.getPlayer(), warningAppealConfiguration.rejectAppealPermission);
    }
}
