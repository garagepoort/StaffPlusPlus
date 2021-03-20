package net.shortninja.staffplus.domain.staff.reporting.gui;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.gui.IAction;
import net.shortninja.staffplus.domain.player.SppPlayer;
import net.shortninja.staffplus.common.gui.AbstractGui;
import net.shortninja.staffplus.common.gui.PagedGui;
import net.shortninja.staffplus.domain.staff.reporting.Report;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AllAssignedReportsGui extends PagedGui {

    public AllAssignedReportsGui(Player player, String title, int page, Supplier<AbstractGui> previousGuiSupplier) {
        super(player, title, page, previousGuiSupplier);
    }

    @Override
    protected AllAssignedReportsGui getNextUi(Player player, SppPlayer target, String title, int page) {
        return new AllAssignedReportsGui(player, title, page, previousGuiSupplier);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                int reportId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
                Report report = IocContainer.getReportService().getReport(reportId);
                new ManageReportGui(player, "Report by: " + report.getReporterName(), report, () -> new AllAssignedReportsGui(player, getTitle(), getCurrentPage(), getPreviousGuiSupplier()))
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
            .getAllAssignedReports(offset, amount)
            .stream()
            .map(ReportItemBuilder::build)
            .collect(Collectors.toList());
    }
}