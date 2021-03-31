package net.shortninja.staffplus.core.domain.staff.reporting.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportService;
import net.shortninja.staffplus.core.domain.staff.reporting.config.ReportTypeConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.getInventorySize;

public class ReportTypeSelectGui extends AbstractGui {
    private final Player staff;
    private final String reason;
    private final SppPlayer targetPlayer;
    private final ReportService reportService = StaffPlus.get().getIocContainer().get(ReportService.class);

    public ReportTypeSelectGui(Player staff, String reason, SppPlayer targetPlayer) {
        super(getInventorySize(StaffPlus.get().getIocContainer().get(Options.class).reportConfiguration.getReportTypeConfigurations().size()), "Select the type of report");
        this.staff = staff;
        this.reason = reason;
        this.targetPlayer = targetPlayer;
    }

    public ReportTypeSelectGui(Player staff, String reason) {
        super(getInventorySize(StaffPlus.get().getIocContainer().get(Options.class).reportConfiguration.getReportTypeConfigurations().size()), "Select the type of report");
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
                String reportType = StaffPlus.get().getIocContainer().get(IProtocolService.class).getVersionProtocol().getNbtString(item);
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