package net.shortninja.staffplus.core.domain.staff.reporting.gui.actions;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportService;
import net.shortninja.staffplus.core.domain.staff.reporting.config.ReportReasonConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class SelectReportReasonAction implements IAction {
    private final ReportReasonConfiguration reportReasonConfiguration;
    private final Player staff;
    private final SppPlayer targetPlayer;
    private final String reportType;
    private final ReportService reportService = StaffPlus.get().getIocContainer().get(ReportService.class);
    private final BukkitUtils bukkitUtils = StaffPlus.get().getIocContainer().get(BukkitUtils.class);

    public SelectReportReasonAction(ReportReasonConfiguration reportReasonConfiguration, Player staff, SppPlayer targetPlayer, String reportType) {
        this.reportReasonConfiguration = reportReasonConfiguration;
        this.staff = staff;
        this.targetPlayer = targetPlayer;
        this.reportType = reportType == null ? reportReasonConfiguration.getReportType().orElse(null) : reportType;
    }


    @Override
    public void click(Player player, ItemStack item, int slot, ClickType clickType) {
        if (targetPlayer == null) {
            bukkitUtils.runTaskAsync(player, () -> reportService.sendReport(staff, reportReasonConfiguration.getReason(), reportType));
        } else {
            bukkitUtils.runTaskAsync(player, () -> reportService.sendReport(staff, targetPlayer, reportReasonConfiguration.getReason(), reportType));
        }
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
