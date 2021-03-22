package net.shortninja.staffplus.core.domain.staff.reporting.gui.actions;

import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.domain.staff.reporting.Report;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportService;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TeleportToReportLocationAction implements IAction {
    private Messages messages = IocContainer.get(Messages.class);
    private MessageCoordinator message = IocContainer.get(MessageCoordinator.class);
    private final Report report;

    public TeleportToReportLocationAction(Report report) {
        this.report = report;
    }

    @Override
    public void click(Player player, ItemStack item, int slot) {
        if (report.getLocation().isPresent()) {
            IocContainer.get(ReportService.class).goToReportLocation(player, report.getId());
        } else {
            message.send(player, "&cLocation not known for this report.", messages.prefixReports);
        }
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
