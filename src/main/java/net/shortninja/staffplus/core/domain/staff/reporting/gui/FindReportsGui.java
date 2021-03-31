package net.shortninja.staffplus.core.domain.staff.reporting.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.gui.PagedGui;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.reporting.Report;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportFilters;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportService;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class FindReportsGui extends PagedGui {

    private final ReportFilters reportFilters;
    private final ReportItemBuilder reportItemBuilder = StaffPlus.get().getIocContainer().get(ReportItemBuilder.class);

    public FindReportsGui(Player player, ReportFilters reportFilters, int page) {
        super(player, "Find reports", page);
        this.reportFilters = reportFilters;
    }

    @Override
    protected FindReportsGui getNextUi(Player player, SppPlayer target, String title, int page) {
        return new FindReportsGui(player, reportFilters, page);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                int reportId = Integer.parseInt(StaffPlus.get().getIocContainer().get(IProtocolService.class).getVersionProtocol().getNbtString(item));
                Report report = StaffPlus.get().getIocContainer().get(ReportService.class).getReport(reportId);
                new ManageReportGui(player, "Report by: " + report.getReporterName(), report, () -> new FindReportsGui(player, reportFilters, getCurrentPage()))
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
        return StaffPlus.get().getIocContainer().get(ReportService.class).findReports(reportFilters, offset, amount).stream()
                .map(reportItemBuilder::build)
                .collect(Collectors.toList());
    }
}