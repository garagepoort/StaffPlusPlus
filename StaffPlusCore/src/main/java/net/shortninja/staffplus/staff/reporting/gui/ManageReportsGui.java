package net.shortninja.staffplus.staff.reporting.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.config.GuiItemConfig;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

import static org.bukkit.Material.PAPER;

public class ManageReportsGui extends AbstractGui {

    private final Options options = IocContainer.getOptions();

    private final GuiItemConfig closedReportsGui;
    private final GuiItemConfig assignedReportsGui;
    private final GuiItemConfig openReportsGui;

    public ManageReportsGui(Player player, String title) {
        super(9, title);

        openReportsGui = options.reportConfiguration.getOpenReportsGui();
        closedReportsGui = options.reportConfiguration.getClosedReportsGui();
        assignedReportsGui = options.reportConfiguration.getMyAssignedReportsGui();

        if (openReportsGui.isEnabled()) {
            setMenuItem(1, buildGuiItem(PAPER, openReportsGui), (p) -> new OpenReportsGui(p, openReportsGui.getTitle(), 0));
            setMenuItem(2, buildGuiItem(PAPER, assignedReportsGui), (p) -> new AssignedReportsGui(p, assignedReportsGui.getTitle(), 0));
            setMenuItem(3, buildGuiItem(PAPER, closedReportsGui), (p) -> new ClosedReportsGui(p, closedReportsGui.getTitle(), 0));
        }

        PlayerSession playerSession = IocContainer.getSessionManager().get(player.getUniqueId());
        player.closeInventory();
        player.openInventory(getInventory());
        playerSession.setCurrentGui(this);
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