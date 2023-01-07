package net.shortninja.staffplus.core.domain.staff.altaccountdetect;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.common.StaffPlusPlusJoinedEvent;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.config.AltDetectConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBukkitListener
public class AltDetectionListener implements Listener {

    private final AltDetectConfiguration altDetectConfiguration;
    private final AltDetectionService altDetectionService;
    private final PlayerManager playerManager;
    private final BukkitUtils bukkitUtils;

    public AltDetectionListener(AltDetectConfiguration altDetectConfiguration, AltDetectionService altDetectionService, PlayerManager playerManager, BukkitUtils bukkitUtils) {
        this.altDetectionService = altDetectionService;
        this.altDetectConfiguration = altDetectConfiguration;
        this.playerManager = playerManager;
        this.bukkitUtils = bukkitUtils;
    }

    @EventHandler
    public void onJoin(StaffPlusPlusJoinedEvent event) {
        if (!altDetectConfiguration.enabled) {
            return;
        }

        playerManager.getOnlinePlayer(event.getPlayer().getUniqueId()).ifPresent(onlinePlayer -> bukkitUtils.runTaskAsync(() -> altDetectionService.detectAltAccount(onlinePlayer)));
    }
}
