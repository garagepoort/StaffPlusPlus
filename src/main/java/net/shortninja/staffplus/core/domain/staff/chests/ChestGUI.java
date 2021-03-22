package net.shortninja.staffplus.core.domain.staff.chests;

import net.shortninja.staffplus.core.common.UpdatableGui;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.PassThroughClickAction;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class ChestGUI extends AbstractGui implements UpdatableGui {

    private static final PassThroughClickAction PASS_THROUGH_ACTION = new PassThroughClickAction();
    private final Inventory targetInventory;
    private SppPlayer targetPlayer;

    private String itemSelectedFrom;
    private int itemSelectedSlot;
    private ChestGuiType chestGuiType;
    private boolean interactionEnabled;

    public ChestGUI(SppPlayer targetPlayer, Inventory container, InventoryType inventoryType, ChestGuiType chestGuiType, boolean interactionEnabled) {
        super("Staff view", inventoryType);
        this.targetInventory = container;
        this.targetPlayer = targetPlayer;
        this.chestGuiType = chestGuiType;
        this.interactionEnabled = interactionEnabled;
    }

    public ChestGUI(SppPlayer targetPlayer, Inventory container, int containerSize, ChestGuiType chestGuiType, boolean interactionEnabled) {
        super(containerSize, "Staff view");
        this.targetInventory = container;
        this.targetPlayer = targetPlayer;
        this.chestGuiType = chestGuiType;
        this.interactionEnabled = interactionEnabled;
    }

    public ChestGUI(Inventory inventory, InventoryType inventoryType, ChestGuiType chestGuiType, boolean interactionEnabled) {
        super("Staff view", inventoryType);
        this.targetInventory = inventory;
        this.chestGuiType = chestGuiType;
        this.interactionEnabled = interactionEnabled;
    }

    public ChestGUI(Inventory container, int containerSize, ChestGuiType chestGuiType, boolean interactionEnabled) {
        super(containerSize, "Staff view");
        this.targetInventory = container;
        this.chestGuiType = chestGuiType;
        this.interactionEnabled = interactionEnabled;
    }

    @Override
    public void buildGui() {
        update();
    }

    @Override
    public void update() {
        if ("player".equalsIgnoreCase(getItemSelectedFrom())) {
            //Stop updating if a staffmember picked up an item.
            //This prevents item duplication
            return;
        }

        for (int i = 0; i < targetInventory.getContents().length; i++) {
            setItem(i, targetInventory.getItem(i), PASS_THROUGH_ACTION);
        }
    }

    public ChestGuiType getChestGuiType() {
        return chestGuiType;
    }

    void setItemSelectedFrom(String itemSelectedFrom) {
        this.itemSelectedFrom = itemSelectedFrom;
    }

    public String getItemSelectedFrom() {
        return itemSelectedFrom;
    }

    void setItemSelectedSlot(int itemSelectedSlot) {
        this.itemSelectedSlot = itemSelectedSlot;
    }

    int getItemSelectedSlot() {
        return itemSelectedSlot;
    }

    public Inventory getTargetInventory() {
        return targetInventory;
    }

    public SppPlayer getTargetPlayer() {
        return targetPlayer;
    }

    public boolean isInteractionEnabled() {
        return interactionEnabled;
    }

}
