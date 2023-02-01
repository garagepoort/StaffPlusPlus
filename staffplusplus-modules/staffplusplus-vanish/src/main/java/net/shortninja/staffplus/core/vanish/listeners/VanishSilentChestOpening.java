package net.shortninja.staffplus.core.vanish.listeners;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.domain.staff.chests.ChestGuiBuilder;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

@IocBukkitListener(conditionalOnProperty = "vanish-module.silent-chest-opening=true")
public class VanishSilentChestOpening implements Listener {

    @ConfigProperty("vanish-module.silent-chest-interaction")
    private boolean silentChestInteraction;
    @ConfigProperty("vanish-module.normal-chest-opening")
    private boolean normalChestOpening;

    private final OnlineSessionsManager sessionManager;
    private final ChestGuiBuilder chestGuiBuilder;

    public VanishSilentChestOpening(OnlineSessionsManager sessionManager, ChestGuiBuilder chestGuiBuilder) {
        this.sessionManager = sessionManager;
        this.chestGuiBuilder = chestGuiBuilder;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(allowWhileSneaking(player)) {
            return;
        }

        OnlinePlayerSession playerSession = sessionManager.get(player);
        if (playerSession.isVanished() && checkingChest(event)) {
            Container container = (Container) event.getClickedBlock().getState();
            chestGuiBuilder.build(container, silentChestInteraction).show(player);
            event.setCancelled(true);
        }
    }

    private boolean allowWhileSneaking(Player player) {
        return player.isSneaking() && normalChestOpening;
    }

    private boolean checkingChest(PlayerInteractEvent event) {
        return event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof Container;
    }
}
