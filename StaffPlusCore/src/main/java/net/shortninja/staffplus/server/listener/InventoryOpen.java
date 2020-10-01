package net.shortninja.staffplus.server.listener;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.staff.protect.ProtectService;
import net.shortninja.staffplus.staff.tracing.TraceService;
import net.shortninja.staffplus.staff.tracing.TraceType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class InventoryOpen implements Listener {

    private static final List<InventoryType> PROTECTED_INVENTORY_TYPES = Arrays.asList(
        InventoryType.CHEST,
        InventoryType.DISPENSER,
        InventoryType.DROPPER,
        InventoryType.FURNACE,
        InventoryType.WORKBENCH,
        InventoryType.ENCHANTING,
        InventoryType.BREWING,
        InventoryType.MERCHANT,
        InventoryType.ENDER_CHEST,
        InventoryType.ANVIL,
        InventoryType.SMITHING,
        InventoryType.BEACON,
        InventoryType.HOPPER,
        InventoryType.SHULKER_BOX,
        InventoryType.BARREL,
        InventoryType.BLAST_FURNACE,
        InventoryType.LECTERN,
        InventoryType.SMOKER,
        InventoryType.LOOM,
        InventoryType.CARTOGRAPHY,
        InventoryType.GRINDSTONE,
        InventoryType.STONECUTTER
    );
    private final TraceService traceService = IocContainer.getTraceService();
    private final ProtectService protectService = IocContainer.getProtectService();

    public InventoryOpen() {
        Bukkit.getServer().getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        HumanEntity player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();
        Inventory inventory = event.getInventory();
        String holder = inventory.getHolder() != null ? inventory.getHolder().getClass().getSimpleName() : "No holder";

        Location location = inventory.getLocation() == null ? player.getLocation() : inventory.getLocation();
        if (shouldPreventInventory(inventory) && protectService.isLocationProtect((Player) player, location)) {
            event.setCancelled(true);
            return;
        }

        traceService.sendTraceMessage(TraceType.INVENTORY, uniqueId,
            String.format("Opened inventory with holder [%s] at location [%s,%s,%s] with content %s",
                holder,
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ(),
                getInventoryContent(inventory))
        );
    }

    private boolean shouldPreventInventory(Inventory inventory) {
        return PROTECTED_INVENTORY_TYPES.contains(inventory.getType());
    }

    private String getInventoryContent(Inventory inventory) {
        return Arrays.stream(inventory.getContents())
            .filter(Objects::nonNull)
            .map(i -> i.getAmount() + " " + i.getType())
            .collect(Collectors.joining(" - "));
    }
}
