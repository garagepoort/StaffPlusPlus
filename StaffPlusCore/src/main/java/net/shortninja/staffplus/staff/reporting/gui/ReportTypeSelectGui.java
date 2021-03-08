package net.shortninja.staffplus.staff.reporting.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.IAction;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.staff.reporting.ReportService;
import net.shortninja.staffplus.staff.reporting.config.ReportTypeConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static net.shortninja.staffplus.IocContainer.getOptions;
import static net.shortninja.staffplus.util.BukkitUtils.getInventorySize;

public class ReportTypeSelectGui extends AbstractGui {
    private final Player staff;
    private final String reason;
    private final SppPlayer targetPlayer;
    private final ReportService reportService = IocContainer.getReportService();

    public ReportTypeSelectGui(Player staff, String reason, SppPlayer targetPlayer) {
        super(getInventorySize(getOptions().reportConfiguration.getReportTypeConfigurations().size()), "Select the type of report");
        this.staff = staff;
        this.reason = reason;
        this.targetPlayer = targetPlayer;
    }

    public ReportTypeSelectGui(Player staff, String reason) {
        super(getInventorySize(getOptions().reportConfiguration.getReportTypeConfigurations().size()), "Select the type of report");
        this.staff = staff;
        this.reason = reason;
        this.targetPlayer = null;
    }

    @Override
    public void buildGui() {
        IAction selectAction = getSelectAction();
        int count = 0;
        for (ReportTypeConfiguration reportTypeConfiguration : options.reportConfiguration.getReportTypeConfigurations()) {
            setItem(count, ReportTypeItemBuilder.build(reportTypeConfiguration), selectAction);
            count++;
        }
    }

    private IAction getSelectAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                String reportType = StaffPlus.get().versionProtocol.getNbtString(item);
                if (targetPlayer == null) {
                    reportService.sendReport(staff, reason, reportType);
                } else {
                    reportService.sendReport(staff, targetPlayer, reason, reportType);
                }
            }

            @Override
            public boolean shouldClose(Player player) {
                return true;
            }
        };
    }
}