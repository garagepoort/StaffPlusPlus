package net.shortninja.staffplus.domain.player.listeners;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.common.utils.InventoryFactory;
import net.shortninja.staffplus.domain.staff.chests.ChestGUI;
import net.shortninja.staffplus.domain.staff.chests.ChestGuiType;
import net.shortninja.staffplus.domain.staff.freeze.FreezeGui;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryClose implements Listener {
    private final Options options = IocContainer.getOptions();
    private final SessionManagerImpl sessionManager = IocContainer.getSessionManager();

    public InventoryClose() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClose(InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();
        PlayerSession playerSession = sessionManager.get(player.getUniqueId());

        if (playerSession.isFrozen() && options.modeConfiguration.getFreezeModeConfiguration().isModeFreezePrompt()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    new FreezeGui(options.modeConfiguration.getFreezeModeConfiguration().getModeFreezePromptTitle()).show(player);
                }
            }.runTaskLater(StaffPlus.get(), 1L);
            return;
        }

        if (playerSession.getCurrentGui().isPresent() && (playerSession.getCurrentGui().get() instanceof ChestGUI)) {
            ChestGUI chestGUI = (ChestGUI) playerSession.getCurrentGui().get();
            if (chestGUI.getChestGuiType() == ChestGuiType.ENDER_CHEST_EXAMINE && chestGUI.getTargetPlayer() != null && !chestGUI.getTargetPlayer().isOnline()) {
                InventoryFactory.saveEnderchestOffline(player, chestGUI.getTargetPlayer(), chestGUI.getTargetInventory());
            }
            if (chestGUI.getChestGuiType() == ChestGuiType.PLAYER_INVENTORY_EXAMINE && chestGUI.getTargetPlayer() != null && !chestGUI.getTargetPlayer().isOnline()) {
                InventoryFactory.saveInventoryOffline(player, chestGUI.getTargetPlayer(), chestGUI.getTargetInventory());
            }
        }

        if (playerSession.getCurrentGui().isPresent()) {
            playerSession.setCurrentGui(null);
        }
    }
}