package net.shortninja.staffplus.player.attribute.gui.hub.reports;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.CommandUtil;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.reporting.Report;
import net.shortninja.staffplus.reporting.ReportPlayerService;
import net.shortninja.staffplus.unordered.IAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MyReportsGui extends AbstractGui {
    private static final int SIZE = 54;
    private UserManager userManager = IocContainer.getUserManager();
    private ReportPlayerService reportPlayerService = IocContainer.getReportPlayerService();

    public MyReportsGui(Player player, String title) {
        super(SIZE, title);

        IAction action = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                CommandUtil.playerAction(player, () -> {
                    int reportId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
                    Report report = reportPlayerService.getReport(reportId);
                    new ManageReportGui(player, "Report by: " + report.getReporterName(), report);
                });
            }

            @Override
            public boolean shouldClose() {
                return false;
            }

            @Override
            public void execute(Player player, String input) {
            }
        };

        int count = 0; // Using this with an enhanced for loop because it is much faster than converting to an array.

        for (Report report : reportPlayerService.getAssignedReports(player.getUniqueId())) {
            if ((count + 1) >= SIZE) {
                break;
            }

            setItem(count, ReportItemBuilder.build(report), action);
            count++;
        }

        player.closeInventory();
        player.openInventory(getInventory());
        userManager.get(player.getUniqueId()).setCurrentGui(this);
    }
}