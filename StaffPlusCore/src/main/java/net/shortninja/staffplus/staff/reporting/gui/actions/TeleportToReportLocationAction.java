package net.shortninja.staffplus.staff.reporting.gui.actions;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.IAction;
import net.shortninja.staffplus.common.cmd.CommandUtil;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.staff.reporting.ManageReportService;
import net.shortninja.staffplus.staff.reporting.Report;
import net.shortninja.staffplus.util.Message;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TeleportToReportLocationAction implements IAction {
    private ManageReportService manageReportService = IocContainer.getManageReportService();
    private Messages messages = IocContainer.getMessages();
    private Message message = IocContainer.getMessage();
    private final Report report;

    public TeleportToReportLocationAction(Report report) {
        this.report = report;
    }

    @Override
    public void click(Player player, ItemStack item, int slot) {
        CommandUtil.playerAction(player, () -> {
            if (report.getLocation().isPresent()) {
                Location location = report.getLocation().get();
                player.teleport(location);
                message.send(player, "You have been teleported to the location where this report was created", messages.prefixReports);
            } else {
                message.send(player, "&cLocation not known for this report.", messages.prefixReports);
            }
        });
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
