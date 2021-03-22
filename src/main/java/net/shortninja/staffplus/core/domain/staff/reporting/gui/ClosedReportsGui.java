package net.shortninja.staffplus.core.domain.staff.reporting.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.gui.PagedGui;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.reporting.ManageReportService;
import net.shortninja.staffplus.core.domain.staff.reporting.Report;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportService;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ClosedReportsGui extends PagedGui {

    private PermissionHandler permission = IocContainer.get(PermissionHandler.class);
    private final ReportItemBuilder reportItemBuilder = IocContainer.get(ReportItemBuilder.class);

    public ClosedReportsGui(Player player, String title, int page, Supplier<AbstractGui> backGuiSupplier) {
        super(player, title, page, backGuiSupplier);
    }

    @Override
    protected ClosedReportsGui getNextUi(Player player, SppPlayer target, String title, int page) {
        return new ClosedReportsGui(player, title, page, this.previousGuiSupplier);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                if (permission.has(player, options.manageReportConfiguration.getPermissionDelete())) {
                    int reportId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
                    Report report = IocContainer.get(ReportService.class).getReport(reportId);
                    new ClosedReportManageGui(player, "Manage closed report", report).show(player);
                }
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };
    }

    @Override
    public List<ItemStack> getItems(Player player, SppPlayer target, int offset, int amount) {
        return IocContainer.get(ManageReportService.class).getClosedReports(offset, amount)
            .stream()
            .map(reportItemBuilder::build)
            .collect(Collectors.toList());
    }
}