package net.shortninja.staffplus.core.domain.staff.ban.appeals;

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

@IocBean(conditionalOnProperty = "ban-module.appeals.enabled=true")
@IocListener
public class BanAppealNotifierListener implements Listener {

    @ConfigProperty("commands:bans.manage.appealed-gui")
    public String commandManageAppealedBansGui;

    private final AppealRepository appealRepository;
    private final BanAppealConfiguration banAppealConfiguration;
    private final PermissionHandler permission;
    private final Messages messages;
    private final BukkitUtils bukkitUtils;
    private final ServerSyncConfiguration serverSyncConfiguration;

    public BanAppealNotifierListener(AppealRepository appealRepository,
                                     BanAppealConfiguration banAppealConfiguration,
                                     PermissionHandler permission,
                                     Messages messages,
                                     BukkitUtils bukkitUtils,
                                     ServerSyncConfiguration serverSyncConfiguration) {
        this.appealRepository = appealRepository;
        this.banAppealConfiguration = banAppealConfiguration;
        this.permission = permission;
        this.messages = messages;
        this.bukkitUtils = bukkitUtils;
        this.serverSyncConfiguration = serverSyncConfiguration;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void notifyAppeals(StaffPlusPlusJoinedEvent event) {
        if (!permission.has(event.getPlayer(), banAppealConfiguration.permissionNotifications)) {
            return;
        }

        bukkitUtils.runTaskAsync(() -> {
            int openAppeals = appealRepository.getCountOpenAppeals(AppealableType.BAN, "sp_banned_players", serverSyncConfiguration.banSyncServers);
            if (openAppeals > 0) {
                sendMessage(event, openAppeals);
            }
        });
    }

    private void sendMessage(StaffPlusPlusJoinedEvent event, int appealsCount) {
        JSONMessage message = JavaUtils.buildClickableMessage(
            messages.banOpenAppealsNotify.replace("%appealsCount%", String.valueOf(appealsCount)),
            "View unresolved ban appeals!",
            "Click to view unresolved appeals",
            commandManageAppealedBansGui,
            canManageBan(event));
        message.send(event.getPlayer());
    }

    private boolean canManageBan(StaffPlusPlusJoinedEvent event) {
        return permission.has(event.getPlayer(), banAppealConfiguration.approveAppealPermission)
            || permission.has(event.getPlayer(), banAppealConfiguration.rejectAppealPermission);
    }
}
