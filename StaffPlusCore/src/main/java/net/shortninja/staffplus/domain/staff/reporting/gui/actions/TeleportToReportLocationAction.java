package net.shortninja.staffplus.domain.staff.reporting.gui.actions;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.gui.IAction;
import net.shortninja.staffplus.common.config.Messages;
import net.shortninja.staffplus.domain.staff.reporting.Report;
import net.shortninja.staffplus.common.utils.MessageCoordinator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TeleportToReportLocationAction implements IAction {
    private Messages messages = IocContainer.getMessages();
    private MessageCoordinator message = IocContainer.getMessage();
    private final Report report;

    public TeleportToReportLocationAction(Report report) {
        this.report = report;
    }

    @Override
    public void click(Player player, ItemStack item, int slot) {
        if (report.getLocation().isPresent()) {
            IocContainer.getReportService().goToReportLocation(player, report.getId());
        } else {
            message.send(player, "&cLocation not known for this report.", messages.prefixReports);
        }
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
