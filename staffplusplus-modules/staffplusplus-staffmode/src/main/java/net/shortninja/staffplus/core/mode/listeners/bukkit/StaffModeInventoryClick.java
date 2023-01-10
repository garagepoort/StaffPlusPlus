package net.shortninja.staffplus.core.mode.listeners.bukkit;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.mode.StaffModeItemsService;
import net.shortninja.staffplus.core.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplus.core.mode.config.ModeItemConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

@IocBukkitListener
public class StaffModeInventoryClick implements Listener {
    private final OnlineSessionsManager sessionManager;
    private final StaffModeItemsService staffModeItemsService;

    public StaffModeInventoryClick(OnlineSessionsManager sessionManager, StaffModeItemsService staffModeItemsService) {
        this.sessionManager = sessionManager;
        this.staffModeItemsService = staffModeItemsService;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        OnlinePlayerSession playerSession = sessionManager.get(player);
        ItemStack item = event.getCurrentItem();

        if (!playerSession.getCurrentGui().isPresent() || item == null) {
            if (playerSession.isInStaffMode()) {
                Optional<GeneralModeConfiguration> mode = playerSession.get("modeConfig");
                if (!mode.get().isModeInventoryInteraction()) {
                    event.setCancelled(true);
                    return;
                }
                Optional<? extends ModeItemConfiguration> module = staffModeItemsService.getModule(item);
                if (module.isPresent() && !module.get().isMovable()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}