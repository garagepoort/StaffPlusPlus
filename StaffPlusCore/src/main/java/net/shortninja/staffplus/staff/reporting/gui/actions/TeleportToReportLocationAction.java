package net.shortninja.staffplus.staff.reporting.gui.actions;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.IAction;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.staff.reporting.Report;
import net.shortninja.staffplus.util.Message;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TeleportToReportLocationAction implements IAction {
    private Messages messages = IocContainer.getMessages();
    private Message message = IocContainer.getMessage();
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
