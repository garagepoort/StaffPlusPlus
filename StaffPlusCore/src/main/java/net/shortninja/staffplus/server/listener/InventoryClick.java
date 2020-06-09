package net.shortninja.staffplus.server.listener;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.unordered.IUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class InventoryClick implements Listener {
    private Options options = StaffPlus.get().options;
    private UserManager userManager = StaffPlus.get().userManager;
    private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;

    public InventoryClick() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        IUser user = userManager.get(uuid);
        ItemStack item = event.getCurrentItem();
        int slot = event.getSlot();

<<<<<<< HEAD
        if(StaffPlus.get().inventoryHandler.isInVirtualInv(uuid)||
            StaffPlus.get().viewedChest.containsKey(event.getInventory())){
            event.setCancelled(true);
        }

=======
        if (StaffPlus.get().inventoryHandler.isInVirtualInv(uuid) ||
                StaffPlus.get().viewedChest.containsKey(event.getInventory())) {
            event.setCancelled(true);
        }

        if (user == null)
            return;
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
        if (!user.getCurrentGui().isPresent() || item == null) {
            if (modeCoordinator.isInMode(uuid) && !options.modeInventoryInteraction) {
                event.setCancelled(true);
            }

            return;
        }

        IAction action = user.getCurrentGui().get().getAction(slot);

        if (action != null) {
            action.click(player, item, slot);

            if (action.shouldClose()) {
                player.closeInventory();
            }
        }

        event.setCancelled(true);
    }
}