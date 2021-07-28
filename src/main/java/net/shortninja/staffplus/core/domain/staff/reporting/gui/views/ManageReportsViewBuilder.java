package net.shortninja.staffplus.core.domain.staff.reporting.gui.views;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.gui.GuiItemConfig;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Material.PAPER;

@IocBean
public class ManageReportsViewBuilder {

    private final GuiItemConfig closedReportsGui;
    private final GuiItemConfig myAssignedReportsGui;
    private final GuiItemConfig assignedReportsGui;
    private final GuiItemConfig openReportsGui;

    public ManageReportsViewBuilder(Options options) {
        openReportsGui = options.reportConfiguration.getOpenReportsGui();
        closedReportsGui = options.reportConfiguration.getClosedReportsGui();
        myAssignedReportsGui = options.reportConfiguration.getMyAssignedReportsGui();
        assignedReportsGui = options.reportConfiguration.getAssignedReportsGui();
    }

    public TubingGui buildGui() {
        TubingGui.Builder builder = new TubingGui.Builder("Manage reports", 9);

        if (openReportsGui.isEnabled()) {
            builder.addItem(getAction("manage-reports/view/open"), 0, buildGuiItem(PAPER, openReportsGui));
        }
        if (myAssignedReportsGui.isEnabled()) {
            builder.addItem(getAction("manage-reports/view/my-assigned"), 1, buildGuiItem(PAPER, myAssignedReportsGui));
        }
        if (assignedReportsGui.isEnabled()) {
            builder.addItem(getAction("manage-reports/view/assigned"), 2, buildGuiItem(PAPER, assignedReportsGui));
        }
        if (closedReportsGui.isEnabled()) {
            builder.addItem(getAction("manage-reports/view/closed"), 3, buildGuiItem(PAPER, closedReportsGui));
        }

        return builder.build();
    }

    private String getAction(String basicAction) {
        return GuiActionBuilder.builder().action(basicAction)
            .param("page", "0")
            .param("backAction", "manage-reports/view/overview")
            .build();
    }

    private ItemStack buildGuiItem(Material material, GuiItemConfig config) {
        return Items.builder()
            .setMaterial(material).setAmount(1)
            .setName(config.getItemName())
            .addLore(config.getItemLore())
            .build();
    }
}
