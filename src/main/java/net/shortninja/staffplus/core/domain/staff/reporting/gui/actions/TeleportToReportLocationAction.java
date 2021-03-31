package net.shortninja.staffplus.core.domain.staff.reporting.gui.actions;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.gui.IAction;

import net.shortninja.staffplus.core.domain.staff.reporting.Report;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportService;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TeleportToReportLocationAction implements IAction {
    private Messages messages = StaffPlus.get().getIocContainer().get(Messages.class);
    private final Report report;

    public TeleportToReportLocationAction(Report report) {
        this.report = report;
    }

    @Override
    public void click(Player player, ItemStack item, int slot) {
        if (report.getLocation().isPresent()) {
            StaffPlus.get().getIocContainer().get(ReportService.class).goToReportLocation(player, report.getId());
        } else {
            messages.send(player, "&cLocation not known for this report.", messages.prefixReports);
        }
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
