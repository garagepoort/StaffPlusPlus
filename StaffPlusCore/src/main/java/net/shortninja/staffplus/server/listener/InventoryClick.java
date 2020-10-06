package net.shortninja.staffplus.server.listener;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.staff.mode.ModeCoordinator;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class InventoryClick implements Listener {
    private final Options options = IocContainer.getOptions();
    private final SessionManager sessionManager = IocContainer.getSessionManager();
    private final ModeCoordinator modeCoordinator = IocContainer.getModeCoordinator();

    public InventoryClick() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        PlayerSession playerSession = sessionManager.get(uuid);
        ItemStack item = event.getCurrentItem();
        int slot = event.getSlot();

        if(StaffPlus.get().inventoryHandler.isInVirtualInv(uuid)||
            StaffPlus.get().viewedChest.contains(event.getInventory())){
            event.setCancelled(true);
        }

        if (!playerSession.getCurrentGui().isPresent() || item == null) {
            if (modeCoordinator.isInMode(uuid) && !options.modeInventoryInteraction) {
                event.setCancelled(true);
            }
            return;
        }

        IAction action = playerSession.getCurrentGui().get().getAction(slot);
        if (action != null) {
            action.click(player, item, slot);
            if (action.shouldClose()) {
                player.closeInventory();
            }
        }

        event.setCancelled(true);
    }
}