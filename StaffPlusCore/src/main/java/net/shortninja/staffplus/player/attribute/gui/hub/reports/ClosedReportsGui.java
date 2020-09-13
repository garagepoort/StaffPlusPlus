package net.shortninja.staffplus.player.attribute.gui.hub.reports;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.reporting.Report;
import net.shortninja.staffplus.reporting.ReportPlayerService;
import org.bukkit.entity.Player;

public class ClosedReportsGui extends AbstractGui {
    private static final int SIZE = 54;
    private UserManager userManager = IocContainer.getUserManager();
    private ReportPlayerService reportPlayerService = IocContainer.getReportPlayerService();

    public ClosedReportsGui(Player player, String title) {
        super(SIZE, title);

        int count = 0; // Using this with an enhanced for loop because it is much faster than converting to an array.

        for (Report report : reportPlayerService.getClosedReports()) {
            if ((count + 1) >= SIZE) {
                break;
            }

            setItem(count, ReportItemBuilder.build(report), null);
            count++;
        }

        player.closeInventory();
        player.openInventory(getInventory());
        userManager.get(player.getUniqueId()).setCurrentGui(this);
    }
}