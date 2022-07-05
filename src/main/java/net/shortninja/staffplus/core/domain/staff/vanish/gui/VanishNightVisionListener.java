package net.shortninja.staffplus.core.domain.staff.vanish.gui;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.staff.nightvision.NightVisionService;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishConfiguration;
import net.shortninja.staffplusplus.vanish.VanishOffEvent;
import net.shortninja.staffplusplus.vanish.VanishOnEvent;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBukkitListener
public class VanishNightVisionListener implements Listener {

    private final VanishConfiguration vanishConfiguration;
    private final NightVisionService nightVisionService;
    private final BukkitUtils bukkitUtils;

    public VanishNightVisionListener(VanishConfiguration vanishConfiguration,
                                     NightVisionService nightVisionService,
                                     BukkitUtils bukkitUtils) {
        this.vanishConfiguration = vanishConfiguration;
        this.nightVisionService = nightVisionService;
        this.bukkitUtils = bukkitUtils;
    }

    @EventHandler
    public void onVanish(VanishOnEvent event) {
        VanishType vanishType = event.getType();
        if (vanishConfiguration.nightVisionEnabled && (vanishType == VanishType.TOTAL || vanishType == VanishType.PLAYER)) {
            bukkitUtils.runTaskLater(() -> nightVisionService.turnOnNightVision("VANISH", event.getPlayer()));
        }
    }

    @EventHandler
    public void onUnvanish(VanishOffEvent event) {
        bukkitUtils.runTaskLater(() -> nightVisionService.turnOffNightVision("VANISH", event.getPlayer()));
    }

}
