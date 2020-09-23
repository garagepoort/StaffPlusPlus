package net.shortninja.staffplus.server.listener;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.staff.freeze.FreezeGui;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryClose implements Listener {
    private final Options options = IocContainer.getOptions();
    private final UserManager userManager = IocContainer.getUserManager();

    public InventoryClose() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClose(InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();
        IUser user = userManager.get(player.getUniqueId());

        if (user == null) {
            return;
        }

        if (user.isFrozen() && options.modeFreezePrompt) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    new FreezeGui(player, options.modeFreezePromptTitle);
                }
            }.runTaskLater(StaffPlus.get(), 1L);
            return;
        }

        if (user.getCurrentGui().isPresent()) {
            user.setCurrentGui(null);
        }

        if (StaffPlus.get().inventoryHandler.isInVirtualInv(event.getPlayer().getUniqueId())) {
            StaffPlus.get().inventoryHandler.removeVirtualUser(event.getPlayer().getUniqueId());
        }
    }
}