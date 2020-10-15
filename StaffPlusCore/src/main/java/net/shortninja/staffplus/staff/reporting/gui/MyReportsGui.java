package net.shortninja.staffplus.staff.reporting.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.attribute.gui.PagedGui;
import net.shortninja.staffplus.unordered.IAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class MyReportsGui extends PagedGui {

    public MyReportsGui(Player player, String title, int page) {
        super(player, title, page);
    }

    @Override
    protected void getNextUi(Player player, String title, int page) {
        new MyReportsGui(player, title, page);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                //Do nothing
            }

            @Override
            public boolean shouldClose() {
                return false;
            }
        };
    }

    @Override
    public List<ItemStack> getItems(Player player, int offset, int amount) {
        return IocContainer.getReportService()
                .getMyReports(player.getUniqueId(), offset, amount)
                .stream()
                .map(ReportItemBuilder::build)
                .collect(Collectors.toList());
    }
}