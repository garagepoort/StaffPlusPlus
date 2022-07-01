package net.shortninja.staffplus.core.domain.staff.mute.appeals;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.staffplusplus.craftbukkit.common.json.rayzr.JSONMessage;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.JsonSenderService;
import net.shortninja.staffplus.core.common.StaffPlusPlusJoinedEvent;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.staff.appeals.database.AppealRepository;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfiguration;
import net.shortninja.staffplusplus.appeals.AppealableType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.List;

@IocBukkitListener(conditionalOnProperty = "mute-module.appeals.enabled=true")
public class MuteAppealNotifierListener implements Listener {

    @ConfigProperty("commands:mutes.manage.appealed-gui")
    public List<String> commandManageAppealedMutesGui;

    private final AppealRepository appealRepository;
    private final MuteAppealConfiguration muteAppealConfiguration;
    private final PermissionHandler permission;
    private final Messages messages;
    private final BukkitUtils bukkitUtils;
    private final ServerSyncConfiguration serverSyncConfiguration;
    private final JsonSenderService jsonSenderService;

    public MuteAppealNotifierListener(AppealRepository appealRepository,
                                      MuteAppealConfiguration muteAppealConfiguration,
                                      PermissionHandler permission,
                                      Messages messages,
                                      BukkitUtils bukkitUtils,
                                      ServerSyncConfiguration serverSyncConfiguration, JsonSenderService jsonSenderService) {
        this.appealRepository = appealRepository;
        this.muteAppealConfiguration = muteAppealConfiguration;
        this.permission = permission;
        this.messages = messages;
        this.bukkitUtils = bukkitUtils;
        this.serverSyncConfiguration = serverSyncConfiguration;
        this.jsonSenderService = jsonSenderService;
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
            commandManageAppealedMutesGui.get(0),
            canManageMute(event));
        jsonSenderService.send(message, event.getPlayer());
    }

    private boolean canManageMute(StaffPlusPlusJoinedEvent event) {
        return permission.has(event.getPlayer(), muteAppealConfiguration.approveAppealPermission)
            || permission.has(event.getPlayer(), muteAppealConfiguration.rejectAppealPermission);
    }
}
