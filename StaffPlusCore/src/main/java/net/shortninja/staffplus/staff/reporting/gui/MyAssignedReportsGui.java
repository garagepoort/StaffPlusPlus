package net.shortninja.staffplus.staff.reporting.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.gui.IAction;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.common.gui.AbstractGui;
import net.shortninja.staffplus.player.attribute.gui.PagedGui;
import net.shortninja.staffplus.staff.reporting.Report;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MyAssignedReportsGui extends PagedGui {

    public MyAssignedReportsGui(Player player, String title, int page, Supplier<AbstractGui> previousGuiSupplier) {
        super(player, title, page, previousGuiSupplier);
    }

    @Override
    protected MyAssignedReportsGui getNextUi(Player player, SppPlayer target, String title, int page) {
        return new MyAssignedReportsGui(player, title, page, previousGuiSupplier);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                int reportId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
                Report report = IocContainer.getReportService().getReport(reportId);
                new ManageReportGui(player, "Report by: " + report.getReporterName(), report, () -> new MyAssignedReportsGui(player, getTitle(), getCurrentPage(), getPreviousGuiSupplier()))
                    .show(player);
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };
    }

    @Override
    public List<ItemStack> getItems(Player player, SppPlayer target, int offset, int amount) {
        return IocContainer.getReportService()
            .getAssignedReports(player.getUniqueId(), offset, amount)
            .stream()
            .map(ReportItemBuilder::build)
            .collect(Collectors.toList());
    }
}