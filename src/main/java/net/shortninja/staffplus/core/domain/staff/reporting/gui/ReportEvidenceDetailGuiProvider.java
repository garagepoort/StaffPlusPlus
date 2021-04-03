package net.shortninja.staffplus.core.domain.staff.reporting.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.evidence.EvidenceDetailGuiProvider;
import net.shortninja.staffplus.core.domain.staff.reporting.Report;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportService;
import net.shortninja.staffplusplus.investigate.EvidenceType;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

@IocBean
@IocMultiProvider(EvidenceDetailGuiProvider.class)
public class ReportEvidenceDetailGuiProvider implements EvidenceDetailGuiProvider {

    private final ReportService reportService;

    public ReportEvidenceDetailGuiProvider(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public AbstractGui get(Player player, SppPlayer target, int id, Supplier<AbstractGui> previousGuiSupplier) {
        Report report = reportService.getReport(id);
        return new ManageReportGui(player, "Report by: " + report.getReporterName(), report, previousGuiSupplier);
    }

    @Override
    public EvidenceType getType() {
        return EvidenceType.REPORT;
    }
}
