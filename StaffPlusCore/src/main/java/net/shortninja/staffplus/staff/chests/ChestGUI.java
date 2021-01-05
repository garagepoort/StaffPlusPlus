package net.shortninja.staffplus.staff.chests;

import net.shortninja.staffplus.common.PassThroughClickAction;
import net.shortninja.staffplus.common.UpdatableGui;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class ChestGUI extends AbstractGui implements UpdatableGui {

    private static final PassThroughClickAction PASS_THROUGH_ACTION = new PassThroughClickAction();
    private final Container container;

    private String itemSelectedFrom;
    private int itemSelectedSlot;

    public ChestGUI(Player player, Container container, InventoryType inventoryType) {
        super("Staff view", inventoryType);
        this.container = container;
        initiate(player);
    }

    public ChestGUI(Player player, Container container, int containerSize) {
        super(containerSize, "Staff view");
        this.container = container;
        initiate(player);
    }

    private void initiate(Player player) {
        update();
        player.closeInventory();
        player.openInventory(getInventory());
        sessionManager.get(player.getUniqueId()).setCurrentGui(this);
    }

    @Override
    public void update() {
        if ("player".equalsIgnoreCase(getItemSelectedFrom())) {
            //Stop updating if a staffmember picked up an item.
            //This prevents item duplication
            return;
        }

        for (int i = 0; i < container.getInventory().getContents().length; i++) {
            setItem(i, container.getInventory().getItem(i), PASS_THROUGH_ACTION);
        }
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

    public Container getContainer() {
        return container;
    }
}
