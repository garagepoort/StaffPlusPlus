package net.shortninja.staffplus.staff.examine;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class ExamineInventoryMove implements Listener {
    private final Options options = IocContainer.getOptions();
    private final SessionManager sessionManager = IocContainer.getSessionManager();
    private PermissionHandler permissionHandler = IocContainer.getPermissionHandler();

    public ExamineInventoryMove() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        PlayerSession playerSession = sessionManager.get(player.getUniqueId());
        if (!playerSession.getCurrentGui().isPresent() || !(playerSession.getCurrentGui().get() instanceof ExamineGui)) {
            return;
        }

        ExamineGui examineGui = (ExamineGui) playerSession.getCurrentGui().get();

        if (!event.isLeftClick()) {
            event.setCancelled(true);
            return;
        }
        if (!isEmptyStack(event.getCursor()) && !isEmptyStack(event.getCurrentItem())) {
            event.setCancelled(true);
            return;
        }

        if (!permissionHandler.has(player, options.permissionExamineViewInventory)) {
            return;
        }

        if (event.getClickedInventory() == examineGui.getInventory() && examineGui.isInventorySlot(event.getSlot())) {
            handleExamineInventoryClick(event, player, examineGui);
        }

        if (event.getClickedInventory() == player.getInventory()) {
            handleStaffInventoryClick(event, player, examineGui);
        }
    }

    private void handleExamineInventoryClick(InventoryClickEvent event, Player player, ExamineGui examineGui) {
        if (!permissionHandler.has(player, options.permissionExamineInventoryInteraction)) {
            event.setCancelled(true);
            return;
        }

        if (!isEmptyStack(event.getCursor()) && isEmptyStack(event.getCurrentItem())) {
            if ("staff".equalsIgnoreCase(examineGui.getItemSelectedFrom())) {
                int playerSlot;
                if (event.getSlot() < ExamineGui.ARMOR_START) {
                    playerSlot = event.getSlot() - ExamineGui.INVENTORY_START;
                } else {
                    //deduct one to take into account the divider item
                    playerSlot = event.getSlot() - ExamineGui.INVENTORY_START - 1;
                }
                examineGui.getTargetPlayer().getInventory().setItem(playerSlot, event.getCursor());
            }
        }

        examineGui.setItemSelectedFrom(null);

        if (isEmptyStack(event.getCursor()) && !isEmptyStack(event.getCurrentItem())) {
            examineGui.setItemSelectedFrom("player");
            examineGui.setItemSelectedSlot(event.getSlot());
        }
    }

    private void handleStaffInventoryClick(InventoryClickEvent event, Player player, ExamineGui examineGui) {
        if (!permissionHandler.has(player, options.permissionExamineInventoryInteraction)) {
            return;
        }

        if (!isEmptyStack(event.getCursor()) && isEmptyStack(event.getCurrentItem())) {
            if ("player".equalsIgnoreCase(examineGui.getItemSelectedFrom())) {
                examineGui.getTargetPlayer().getInventory().setItem(examineGui.getItemSelectedSlot(), null);
            }
        }

        examineGui.setItemSelectedFrom(null);

        if (isEmptyStack(event.getCursor()) && !isEmptyStack(event.getCurrentItem())) {
            examineGui.setItemSelectedFrom("staff");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void dropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!permissionHandler.has(player, options.permissionExamineViewInventory) || !permissionHandler.has(player, options.permissionExamineInventoryInteraction)) {
            return;
        }

        PlayerSession playerSession = sessionManager.get(player.getUniqueId());
        if (!playerSession.getCurrentGui().isPresent() || !(playerSession.getCurrentGui().get() instanceof ExamineGui)) {
            return;
        }
        ExamineGui examineGui = (ExamineGui) playerSession.getCurrentGui().get();

        if ("player".equalsIgnoreCase(examineGui.getItemSelectedFrom())) {
            examineGui.setItemSelectedFrom(null);
            examineGui.getTargetPlayer().getInventory().setItem(examineGui.getItemSelectedSlot(), null);
        }
    }

    private boolean isEmptyStack(ItemStack cursor) {
        return cursor == null || cursor.getType() == Material.AIR;
    }
}