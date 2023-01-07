package net.shortninja.staffplus.core.domain.staff.mute.gui;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import be.garagepoort.staffplusplus.craftbukkit.common.json.rayzr.JSONMessage;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.JsonSenderService;
import net.shortninja.staffplus.core.common.StaffPlusPlusJoinedEvent;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.staff.mute.Mute;
import net.shortninja.staffplus.core.domain.staff.mute.MuteService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Optional;

@IocBukkitListener(conditionalOnProperty = "mute-module.enabled=true")
public class MuteNotifierListener implements Listener {

    @ConfigProperty("%lang%:mute-notify")
    private String muteNotifyMessage;
    @ConfigProperty("commands:my-mutes")
    private List<String> myMutesCmd;
    @ConfigProperty("permissions:view-my-mutes")
    private String myMutesPermission;

    private final PermissionHandler permission;
    private final BukkitUtils bukkitUtils;
    private final MuteService muteService;
    private final JsonSenderService jsonSenderService;

    public MuteNotifierListener(PermissionHandler permission, BukkitUtils bukkitUtils, MuteService muteService, JsonSenderService jsonSenderService) {
        this.permission = permission;
        this.bukkitUtils = bukkitUtils;
        this.muteService = muteService;
        this.jsonSenderService = jsonSenderService;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void notifyMuted(StaffPlusPlusJoinedEvent event) {
        bukkitUtils.runTaskAsync(() -> {
            Optional<Mute> mute = muteService.getMuteByMutedUuid(event.getPlayer().getUniqueId());
            if (mute.isPresent() && !mute.get().isSoftMute()) {
                sendMessage(event);
            }
        });
    }

    private void sendMessage(StaffPlusPlusJoinedEvent event) {
        JSONMessage message = JavaUtils.buildClickableMessage(
            muteNotifyMessage,
            "View your mute!",
            "Click to view your mute",
            myMutesCmd.get(0),
            permission.has(event.getPlayer(), myMutesPermission));
        jsonSenderService.send(message, event.getPlayer());
    }
}
