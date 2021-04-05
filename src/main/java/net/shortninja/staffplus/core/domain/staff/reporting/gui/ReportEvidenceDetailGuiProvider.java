package net.shortninja.staffplus.core.domain.staff.reporting.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.staff.reporting.Report;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportService;
import net.shortninja.staffplusplus.investigate.evidence.EvidenceGuiClick;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@IocBean
@IocMultiProvider(EvidenceGuiClick.class)
public class ReportEvidenceDetailGuiProvider implements EvidenceGuiClick {

    private final ReportService reportService;

    public ReportEvidenceDetailGuiProvider(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public void onClick(Player player, SppPlayer target, int id, Runnable back) {
        Report report = reportService.getReport(id);
        ManageReportGui manageReportGui = new ManageReportGui(player, "Report by: " + report.getReporterName(), report, null);
        manageReportGui.show(player);
        manageReportGui.setItem(49, Items.createBackDoor(), new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot, ClickType clickType) {
                back.run();
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        });
    }

    @Override
    public String getType() {
        return "REPORT";
    }
}
