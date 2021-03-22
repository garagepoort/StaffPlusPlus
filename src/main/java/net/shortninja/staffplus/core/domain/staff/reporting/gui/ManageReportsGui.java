package net.shortninja.staffplus.core.domain.staff.reporting.gui;

import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.GuiItemConfig;
import net.shortninja.staffplus.core.common.gui.IAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

import static org.bukkit.Material.PAPER;

public class ManageReportsGui extends AbstractGui {

    private final Options options = IocContainer.get(Options.class);

    private final GuiItemConfig closedReportsGui;
    private final GuiItemConfig myReportsGui;
    private final GuiItemConfig assignedReportsGui;
    private final GuiItemConfig openReportsGui;
    private final Player player;

    public ManageReportsGui(Player player, String title) {
        super(9, title);
        this.player = player;
        openReportsGui = options.reportConfiguration.getOpenReportsGui();
        closedReportsGui = options.reportConfiguration.getClosedReportsGui();
        myReportsGui = options.reportConfiguration.getMyReportsGui();
        assignedReportsGui = options.reportConfiguration.getAssignedReportsGui();
    }

    @Override
    public void buildGui() {
        if (openReportsGui.isEnabled()) {
            setMenuItem(0, buildGuiItem(PAPER, openReportsGui), (p) -> new OpenReportsGui(p, openReportsGui.getTitle(), 0, () -> new ManageReportsGui(player, getTitle())).show(p));
        }
        if(myReportsGui.isEnabled()) {
            setMenuItem(1, buildGuiItem(PAPER, myReportsGui), (p) -> new MyAssignedReportsGui(p, myReportsGui.getTitle(), 0, () -> new ManageReportsGui(player, getTitle())).show(p));
        }
        if(assignedReportsGui.isEnabled()) {
            setMenuItem(2, buildGuiItem(PAPER, assignedReportsGui), (p) -> new AllAssignedReportsGui(p, assignedReportsGui.getTitle(), 0, () -> new ManageReportsGui(player, getTitle())).show(p));
        }
        if(closedReportsGui.isEnabled()) {
            setMenuItem(3, buildGuiItem(PAPER, closedReportsGui), (p) -> new ClosedReportsGui(p, closedReportsGui.getTitle(), 0, () -> new ManageReportsGui(player, getTitle())).show(p));
        }
    }

    private void setMenuItem(int menuSlot, ItemStack menuItem, Consumer<Player> guiFunction) {
        setItem(menuSlot, menuItem, new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                guiFunction.accept(player);
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        });
    }

    private ItemStack buildGuiItem(Material material, GuiItemConfig config) {
        return Items.builder()
            .setMaterial(material).setAmount(1)
            .setName(config.getItemName())
            .addLore(config.getItemLore())
            .build();
    }
}