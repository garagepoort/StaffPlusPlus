package net.shortninja.staffplus.server.listener;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.staff.chests.ChestGUI;
import net.shortninja.staffplus.staff.chests.ChestGuiType;
import net.shortninja.staffplus.staff.freeze.FreezeGui;
import net.shortninja.staffplus.util.factory.InventoryFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryClose implements Listener {
    private final Options options = IocContainer.getOptions();
    private final SessionManager sessionManager = IocContainer.getSessionManager();

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
                    new FreezeGui(player, options.modeConfiguration.getFreezeModeConfiguration().getModeFreezePromptTitle());
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