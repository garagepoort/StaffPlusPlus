package net.shortninja.staffplus.core.domain.staff.reporting.gui.actions;

import net.shortninja.staffplus.core.StaffPlus;
import be.garagepoort.mcioc.IocContainer;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.staff.reporting.ManageReportService;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DeleteReportAction implements IAction {
    private ManageReportService manageReportService = StaffPlus.get().iocContainer.get(ManageReportService.class);

    @Override
    public void click(Player player, ItemStack item, int slot) {
        int reportId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
        manageReportService.deleteReport(player, reportId);
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
