package net.shortninja.staffplus.core.vanish.listeners;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import be.garagepoort.mcioc.tubingbukkit.config.transformers.ToMaterials;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Set;

@IocBukkitListener(conditionalOnProperty = "vanish-module.enabled=true")
public class VanishCancelInteraction implements Listener {
    private final OnlineSessionsManager sessionManager;

    @ConfigProperty("vanish-module.cancelled-interaction-types")
    @ConfigTransformer(ToMaterials.class)
    private Set<Material> cancelledInteractions;

    public VanishCancelInteraction(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            Player player = event.getPlayer();
            PlayerSession playerSession = sessionManager.get(player);
            if (playerSession.isVanished()) {
                if (cancelledInteractions.contains(event.getClickedBlock().getBlockData().getMaterial())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}