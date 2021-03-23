package net.shortninja.staffplus.core.domain.staff.altaccountdetect;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Options;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@IocBean
public class AltDetectionListener implements Listener {

    private final Options options;
    private final AltDetectionService altDetectionService;

    public AltDetectionListener(Options options, AltDetectionService altDetectionService) {
        this.options = options;
        this.altDetectionService = altDetectionService;
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(!options.altDetectConfiguration.isEnabled()) {
            return;
        }

        altDetectionService.detectAltAccount(event.getPlayer());
    }
}
