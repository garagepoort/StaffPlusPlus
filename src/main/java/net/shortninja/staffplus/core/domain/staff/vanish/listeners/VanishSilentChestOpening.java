package net.shortninja.staffplus.core.domain.staff.vanish.listeners;

import be.garagepoort.mcioc.IocListener;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.domain.staff.chests.ChestGuiBuilder;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

@IocListener(conditionalOnProperty = "vanish-module.silent-chest-opening=true")
public class VanishSilentChestOpening implements Listener {

    @ConfigProperty("vanish-module.silent-chest-interaction")
    private boolean silentChestInteraction;

    private final OnlineSessionsManager sessionManager;
    private final ChestGuiBuilder chestGuiBuilder;

    public VanishSilentChestOpening(OnlineSessionsManager sessionManager, ChestGuiBuilder chestGuiBuilder) {
        this.sessionManager = sessionManager;
        this.chestGuiBuilder = chestGuiBuilder;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        OnlinePlayerSession playerSession = sessionManager.get(player);

        if (playerSession.isVanished() && checkingChest(event) && !player.isSneaking()) {
            Container container = (Container) event.getClickedBlock().getState();
            chestGuiBuilder.build(container, silentChestInteraction).show(player);
            event.setCancelled(true);
        }
    }

    private boolean checkingChest(PlayerInteractEvent event) {
        return event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof Container;
    }
}
