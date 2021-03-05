package net.shortninja.staffplus.staff.reporting.gui.actions;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.IAction;
import net.shortninja.staffplus.common.cmd.CommandUtil;
import net.shortninja.staffplus.staff.reporting.ManageReportService;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ReopenReportAction implements IAction {
    private ManageReportService manageReportService = IocContainer.getManageReportService();

    @Override
    public void click(Player player, ItemStack item, int slot) {
        CommandUtil.playerAction(player, () -> {
            int reportId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
            manageReportService.reopenReport(player, reportId);
        });
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
