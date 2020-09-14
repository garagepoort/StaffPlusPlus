package net.shortninja.staffplus.player.attribute.gui.hub.reports;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.attribute.gui.PagedGui;
import net.shortninja.staffplus.unordered.IAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class ClosedReportsGui extends PagedGui {

    public ClosedReportsGui(Player player, String title, int page) {
        super(player, title, page);
    }

    @Override
    protected void getNextUi(Player player, String title, int page) {
        new ClosedReportsGui(player, title, page);
    }

    @Override
    public IAction getAction() {
        return null;
    }

    @Override
    public List<ItemStack> getItems(Player player, int offset, int amount) {
        return IocContainer.getReportService().getClosedReports(offset, amount)
                .stream()
                .map(ReportItemBuilder::build)
                .collect(Collectors.toList());
    }
}