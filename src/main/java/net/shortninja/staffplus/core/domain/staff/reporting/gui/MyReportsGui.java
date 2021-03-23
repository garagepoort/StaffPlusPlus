package net.shortninja.staffplus.core.domain.staff.reporting.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.gui.PagedGui;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportService;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class MyReportsGui extends PagedGui {

    private final ReportItemBuilder reportItemBuilder = StaffPlus.get().iocContainer.get(ReportItemBuilder.class);

    public MyReportsGui(Player player, String title, int page) {
        super(player, title, page);
    }

    @Override
    protected MyReportsGui getNextUi(Player player, SppPlayer target, String title, int page) {
        return new MyReportsGui(player, title, page);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                //Do nothing
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };
    }

    @Override
    public List<ItemStack> getItems(Player player, SppPlayer target, int offset, int amount) {
        return StaffPlus.get().iocContainer.get(ReportService.class)
                .getMyReports(player.getUniqueId(), offset, amount)
                .stream()
                .map(reportItemBuilder::build)
                .collect(Collectors.toList());
    }
}