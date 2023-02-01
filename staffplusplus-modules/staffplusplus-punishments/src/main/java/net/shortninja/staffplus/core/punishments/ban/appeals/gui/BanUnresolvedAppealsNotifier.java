package net.shortninja.staffplus.core.punishments.ban.appeals.gui;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import be.garagepoort.staffplusplus.craftbukkit.common.json.rayzr.JSONMessage;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.JsonSenderService;
import net.shortninja.staffplus.core.common.StaffPlusPlusJoinedEvent;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.appeals.database.AppealRepository;
import net.shortninja.staffplus.core.punishments.ban.appeals.BanAppealConfiguration;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfiguration;
import net.shortninja.staffplusplus.appeals.AppealableType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.List;

@IocBukkitListener(conditionalOnProperty = "ban-module.appeals.enabled=true")
public class BanUnresolvedAppealsNotifier implements Listener {

    @ConfigProperty("commands:bans.manage.appealed-gui")
    public List<String> commandManageAppealedBansGui;

    private final AppealRepository appealRepository;
    private final BanAppealConfiguration banAppealConfiguration;
    private final PermissionHandler permission;
    private final Messages messages;
    private final BukkitUtils bukkitUtils;
    private final ServerSyncConfiguration serverSyncConfiguration;
    private final JsonSenderService jsonSenderService;

    public BanUnresolvedAppealsNotifier(AppealRepository appealRepository,
                                        BanAppealConfiguration banAppealConfiguration,
                                        PermissionHandler permission,
                                        Messages messages,
                                        BukkitUtils bukkitUtils,
                                        ServerSyncConfiguration serverSyncConfiguration,
                                        JsonSenderService jsonSenderService) {
        this.appealRepository = appealRepository;
        this.banAppealConfiguration = banAppealConfiguration;
        this.permission = permission;
        this.messages = messages;
        this.bukkitUtils = bukkitUtils;
        this.serverSyncConfiguration = serverSyncConfiguration;
        this.jsonSenderService = jsonSenderService;
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
            commandManageAppealedBansGui.get(0),
            canManageBan(event));
        jsonSenderService.send(message, event.getPlayer());
    }

    private boolean canManageBan(StaffPlusPlusJoinedEvent event) {
        return permission.has(event.getPlayer(), banAppealConfiguration.approveAppealPermission)
            || permission.has(event.getPlayer(), banAppealConfiguration.rejectAppealPermission);
    }
}
