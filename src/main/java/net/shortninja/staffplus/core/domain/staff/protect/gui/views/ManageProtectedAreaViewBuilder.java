package net.shortninja.staffplus.core.domain.staff.protect.gui.views;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.model.TubingGui;
import be.garagepoort.mcioc.gui.model.TubingGuiActions;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.domain.staff.protect.ProtectedArea;
import org.bukkit.inventory.ItemStack;

@IocBean
public class ManageProtectedAreaViewBuilder {
    private static final int SIZE = 54;

    public TubingGui buildGui(ProtectedArea protectedArea, String backAction) {
        TubingGui.Builder builder = new TubingGui.Builder("Protected area: " + protectedArea.getName(), SIZE);

        builder.addItem(TubingGuiActions.NOOP, 13, ProtectedAreaItemBuilder.build(protectedArea));

        ItemStack teleportItem = Items.createOrangeColoredGlass("Teleport", "Click to teleport yourself to this area");
        String teleportAction = "protected-areas/teleport?areaId=" + protectedArea.getId();
        builder.addItem(teleportAction, 34, teleportItem);
        builder.addItem(teleportAction, 35, teleportItem);
        builder.addItem(teleportAction, 43, teleportItem);
        builder.addItem(teleportAction, 44, teleportItem);

        ItemStack deleteItem = Items.createRedColoredGlass("Delete protected area", "Click to delete the protected area");
        String deleteAction = "protected-areas/delete?areaId=" + protectedArea.getId();
        builder.addItem(deleteAction, 30, deleteItem);
        builder.addItem(deleteAction, 31, deleteItem);
        builder.addItem(deleteAction, 32, deleteItem);
        builder.addItem(deleteAction, 39, deleteItem);
        builder.addItem(deleteAction, 40, deleteItem);
        builder.addItem(deleteAction, 41, deleteItem);

        if (backAction != null) {
            builder.addItem(backAction, 49, Items.createDoor("Back", "Go back"));
        }
        return builder.build();
    }

}