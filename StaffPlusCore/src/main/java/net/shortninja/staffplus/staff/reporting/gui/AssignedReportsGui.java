package net.shortninja.staffplus.staff.reporting.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.cmd.CommandUtil;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.player.attribute.gui.PagedGui;
import net.shortninja.staffplus.staff.reporting.Report;
import net.shortninja.staffplus.common.IAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AssignedReportsGui extends PagedGui {

    public AssignedReportsGui(Player player, String title, int page, Supplier<AbstractGui> previousGuiSupplier) {
        super(player, title, page, previousGuiSupplier);
    }

    @Override
    protected AssignedReportsGui getNextUi(Player player, SppPlayer target, String title, int page) {
        return new AssignedReportsGui(player, title, page, previousGuiSupplier);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                CommandUtil.playerAction(player, () -> {
                    int reportId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
                    Report report = IocContainer.getReportService().getReport(reportId);
                    new ManageReportGui(player, "Report by: " + report.getReporterName(), report, () -> new AssignedReportsGui(player, getTitle(), getCurrentPage(), getPreviousGuiSupplier()))
                        .show(player);
                });
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