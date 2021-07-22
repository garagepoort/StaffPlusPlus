package net.shortninja.staffplus.core.domain.staff.altaccountdetect;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.config.AltDetectConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@IocBean
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
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!altDetectConfiguration.enabled) {
            return;
        }

        playerManager.getOnlinePlayer(event.getPlayer().getUniqueId()).ifPresent(onlinePlayer -> bukkitUtils.runTaskAsync(() -> altDetectionService.detectAltAccount(onlinePlayer)));
    }
}
