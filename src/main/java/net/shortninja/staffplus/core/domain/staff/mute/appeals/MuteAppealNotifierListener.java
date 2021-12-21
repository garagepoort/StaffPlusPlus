package net.shortninja.staffplus.core.domain.staff.mute.appeals;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import me.rayzr522.jsonmessage.JSONMessage;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.StaffPlusPlusJoinedEvent;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.staff.appeals.database.AppealRepository;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfiguration;
import net.shortninja.staffplusplus.appeals.AppealableType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@IocBean(conditionalOnProperty = "mute-module.appeals.enabled=true")
@IocListener
public class MuteAppealNotifierListener implements Listener {

    @ConfigProperty("commands:mutes.manage.appealed-gui")
    public String commandManageAppealedMutesGui;

    private final AppealRepository appealRepository;
    private final MuteAppealConfiguration muteAppealConfiguration;
    private final PermissionHandler permission;
    private final Messages messages;
    private final BukkitUtils bukkitUtils;
    private final ServerSyncConfiguration serverSyncConfiguration;

    public MuteAppealNotifierListener(AppealRepository appealRepository,
                                      MuteAppealConfiguration muteAppealConfiguration,
                                      PermissionHandler permission,
                                      Messages messages,
                                      BukkitUtils bukkitUtils,
                                      ServerSyncConfiguration serverSyncConfiguration) {
        this.appealRepository = appealRepository;
        this.muteAppealConfiguration = muteAppealConfiguration;
        this.permission = permission;
        this.messages = messages;
        this.bukkitUtils = bukkitUtils;
        this.serverSyncConfiguration = serverSyncConfiguration;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void notifyAppeals(StaffPlusPlusJoinedEvent event) {
        if (!permission.has(event.getPlayer(), muteAppealConfiguration.permissionNotifications)) {
            return;
        }

        bukkitUtils.runTaskAsync(() -> {
            int openAppeals = appealRepository.getCountOpenAppeals(AppealableType.MUTE, "sp_muted_players", serverSyncConfiguration.muteSyncServers);
            if (openAppeals > 0) {
                sendMessage(event, openAppeals);
            }
        });
    }

    private void sendMessage(StaffPlusPlusJoinedEvent event, int appealsCount) {
        JSONMessage message = JavaUtils.buildClickableMessage(
            messages.muteOpenAppealsNotify.replace("%appealsCount%", String.valueOf(appealsCount)),
            "View unresolved mute appeals!",
            "Click to view unresolved appeals",
            commandManageAppealedMutesGui,
            canManageMute(event));
        message.send(event.getPlayer());
    }

    private boolean canManageMute(StaffPlusPlusJoinedEvent event) {
        return permission.has(event.getPlayer(), muteAppealConfiguration.approveAppealPermission)
            || permission.has(event.getPlayer(), muteAppealConfiguration.rejectAppealPermission);
    }
}
