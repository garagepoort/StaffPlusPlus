package net.shortninja.staffplus.core.domain.staff.altaccountdetect;

import net.shortninja.staffplus.core.StaffPlus;
import be.garagepoort.mcioc.IocContainer;
import net.shortninja.staffplus.core.common.config.Options;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AltDetectionListener implements Listener {

    private final Options options = IocContainer.get(Options.class);
    private final AltDetectionService altDetectionService = IocContainer.get(AltDetectionService.class);

    public AltDetectionListener() {
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
