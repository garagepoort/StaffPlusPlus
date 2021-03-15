package net.shortninja.staffplus.staff.reporting.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.common.gui.AbstractGui;
import net.shortninja.staffplus.player.attribute.gui.PagedGui;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.reporting.Report;
import net.shortninja.staffplus.common.gui.IAction;
import net.shortninja.staffplus.util.Permission;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ClosedReportsGui extends PagedGui {

    private Permission permission = IocContainer.getPermissionHandler();
    private Options options = IocContainer.getOptions();

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
                    Report report = IocContainer.getReportService().getReport(reportId);
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
        return IocContainer.getManageReportService().getClosedReports(offset, amount)
            .stream()
            .map(ReportItemBuilder::build)
            .collect(Collectors.toList());
    }
}