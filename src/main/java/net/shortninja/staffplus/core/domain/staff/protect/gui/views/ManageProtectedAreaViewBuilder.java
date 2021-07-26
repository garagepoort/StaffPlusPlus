package net.shortninja.staffplus.core.domain.staff.protect.gui.views;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.TubingGui;
import be.garagepoort.mcioc.gui.TubingGuiActions;
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
        builder.addItem("protected-areas/teleport?areaId=" + protectedArea.getId(), 34, teleportItem);
        builder.addItem("protected-areas/teleport?areaId=" + protectedArea.getId(), 35, teleportItem);
        builder.addItem("protected-areas/teleport?areaId=" + protectedArea.getId(), 43, teleportItem);
        builder.addItem("protected-areas/teleport?areaId=" + protectedArea.getId(), 44, teleportItem);

        ItemStack deleteItem = Items.createRedColoredGlass("Delete protected area", "Click to delete the protected area");
        builder.addItem("protected-areas/delete?areaId=" + protectedArea.getId(), 30, deleteItem);
        builder.addItem("protected-areas/delete?areaId=" + protectedArea.getId(), 31, deleteItem);
        builder.addItem("protected-areas/delete?areaId=" + protectedArea.getId(), 32, deleteItem);
        builder.addItem("protected-areas/delete?areaId=" + protectedArea.getId(), 39, deleteItem);
        builder.addItem("protected-areas/delete?areaId=" + protectedArea.getId(), 40, deleteItem);
        builder.addItem("protected-areas/delete?areaId=" + protectedArea.getId(), 41, deleteItem);

        if (backAction != null) {
            builder.addItem(backAction, 49, Items.createDoor("Back", "Go back"));
        }
        return builder.build();
    }

}