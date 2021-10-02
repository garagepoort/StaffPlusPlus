package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.utils.InventoryFactory;
import net.shortninja.staffplus.core.domain.staff.chests.ChestGUI;
import net.shortninja.staffplus.core.domain.staff.chests.ChestGuiType;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeGui;
import net.shortninja.staffplus.core.domain.staff.freeze.config.FreezeConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

@IocBean
@IocListener
public class InventoryClose implements Listener {
    private final OnlineSessionsManager sessionManager;
    private final InventoryFactory inventoryFactory;
    private final FreezeConfiguration freezeConfiguration;

    public InventoryClose(OnlineSessionsManager sessionManager,
                          InventoryFactory inventoryFactory,
                          FreezeConfiguration freezeConfiguration) {
        this.sessionManager = sessionManager;
        this.inventoryFactory = inventoryFactory;
        this.freezeConfiguration = freezeConfiguration;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClose(InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();
        // Do not run if session does not exists.
        // Reason is that it can result in the session being initialized on the main thread.
        if (sessionManager.has(player.getUniqueId())) {
            OnlinePlayerSession playerSession = sessionManager.get(player);

            if (playerSession.isFrozen() && freezeConfiguration.prompt) {
                Bukkit.getScheduler().runTaskLater(StaffPlus.get(), () -> new FreezeGui(freezeConfiguration.promptTitle).show(player), 1);
                return;
            }

            if (playerSession.getCurrentGui().isPresent() && (playerSession.getCurrentGui().get() instanceof ChestGUI)) {
                ChestGUI chestGUI = (ChestGUI) playerSession.getCurrentGui().get();
                Bukkit.getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
                    if (chestGUI.getChestGuiType() == ChestGuiType.ENDER_CHEST_EXAMINE && chestGUI.getTargetPlayer() != null && !chestGUI.getTargetPlayer().isOnline()) {
                        inventoryFactory.saveEnderchestOffline(chestGUI.getTargetPlayer(), chestGUI.getTargetInventory());
                    }
                    if (chestGUI.getChestGuiType() == ChestGuiType.PLAYER_INVENTORY_EXAMINE && chestGUI.getTargetPlayer() != null && !chestGUI.getTargetPlayer().isOnline()) {
                        inventoryFactory.saveInventoryOffline(chestGUI.getTargetPlayer(), chestGUI.getTargetInventory());
                    }
                });
            }

            if (playerSession.getCurrentGui().isPresent()) {
                playerSession.setCurrentGui(null);
            }
        }

    }
}