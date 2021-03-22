package net.shortninja.staffplus.core.domain.staff.chests;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public class ChestGuiMove implements Listener {
    private final Options options = IocContainer.get(Options.class);
    private final SessionManagerImpl sessionManager = IocContainer.get(SessionManagerImpl.class);
    private PermissionHandler permissionHandler = IocContainer.get(PermissionHandler.class);

    public ChestGuiMove() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        PlayerSession playerSession = sessionManager.get(player.getUniqueId());
        if (!playerSession.getCurrentGui().isPresent() || !(playerSession.getCurrentGui().get() instanceof ChestGUI)) {
            return;
        }

        ChestGUI chestGUI = (ChestGUI) playerSession.getCurrentGui().get();
        if(!chestGUI.isInteractionEnabled()) {
            event.setCancelled(true);
            return;
        }

        if (event.getClick() != ClickType.LEFT) {
            event.setCancelled(true);
            return;
        }

        if (!isEmptyStack(event.getCursor()) && !isEmptyStack(event.getCurrentItem())) {
            event.setCancelled(true);
            return;
        }

        if (event.getClickedInventory() != null && event.getClickedInventory().equals(chestGUI.getInventory())) {
            handleChestInventoryClick(event, player, chestGUI);
        }

        if (event.getClickedInventory() != null && event.getClickedInventory().equals(player.getInventory())) {
            handleStaffInventoryClick(event, player, chestGUI);
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void dragItem(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        PlayerSession playerSession = sessionManager.get(player.getUniqueId());
        if (!playerSession.getCurrentGui().isPresent() || !(playerSession.getCurrentGui().get() instanceof ChestGUI)) {
            return;
        }
        event.setCancelled(true);
    }

    private void handleChestInventoryClick(InventoryClickEvent event, Player player, ChestGUI chestGUI) {
        if (!isEmptyStack(event.getCursor()) && isEmptyStack(event.getCurrentItem())) {
            if ("staff".equalsIgnoreCase(chestGUI.getItemSelectedFrom())) {
                chestGUI.getTargetInventory().setItem(event.getSlot(), event.getCursor());
            }
        }

        chestGUI.setItemSelectedFrom(null);

        if (isEmptyStack(event.getCursor()) && !isEmptyStack(event.getCurrentItem())) {
            chestGUI.setItemSelectedFrom("player");
            chestGUI.setItemSelectedSlot(event.getSlot());
        }
    }

    private void handleStaffInventoryClick(InventoryClickEvent event, Player player, ChestGUI chestGUI) {
        if (!isEmptyStack(event.getCursor()) && isEmptyStack(event.getCurrentItem())) {
            if ("player".equalsIgnoreCase(chestGUI.getItemSelectedFrom())) {
                chestGUI.getTargetInventory().setItem(chestGUI.getItemSelectedSlot(), null);
            }
        }

        chestGUI.setItemSelectedFrom(null);

        if (isEmptyStack(event.getCursor()) && !isEmptyStack(event.getCurrentItem())) {
            chestGUI.setItemSelectedFrom("staff");
        }
    }

    private boolean isEmptyStack(ItemStack cursor) {
        return cursor == null || cursor.getType() == Material.AIR;
    }
}