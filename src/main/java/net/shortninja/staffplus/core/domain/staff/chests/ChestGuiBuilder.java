package net.shortninja.staffplus.core.domain.staff.chests;

import be.garagepoort.mcioc.IocBean;
import org.bukkit.Bukkit;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Container;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.event.inventory.InventoryType;

@IocBean
public class ChestGuiBuilder {

    public ChestGUI build(Container container, boolean enableInteraction) {
        ChestGUI chestGUI;
        if (container instanceof Furnace) {
            chestGUI = new ChestGUI(container.getInventory(), InventoryType.FURNACE, ChestGuiType.CONTAINER, enableInteraction);
        } else if (container instanceof BrewingStand) {
            chestGUI = new ChestGUI(container.getInventory(), InventoryType.BREWING, ChestGuiType.CONTAINER, enableInteraction);
        } else if (container instanceof Dispenser || container instanceof Dropper) {
            chestGUI = new ChestGUI(container.getInventory(), InventoryType.DISPENSER, ChestGuiType.CONTAINER, enableInteraction);
        } else if (container instanceof Hopper) {
            chestGUI = new ChestGUI(container.getInventory(), InventoryType.HOPPER, ChestGuiType.CONTAINER, enableInteraction);
        } else {
            // Either Chest, Chest-like or new block.
            // If it's a non-standard size for some reason, make it work with chests naively and show it. - Will produce errors with onClose() tho.
            int containerSize = container.getInventory().getSize();
            if (containerSize % 9 != 0) {
                Bukkit.getLogger().warning("Non-standard container, expecting an exception below.");
                containerSize += (9 - containerSize % 9);
            }
            chestGUI = new ChestGUI(container.getInventory(), containerSize, ChestGuiType.CONTAINER, enableInteraction);
        }
        return chestGUI;
    }
}
