package net.shortninja.staffplus.core.domain.staff.reporting.gui.views;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.common.gui.PagedGuiBuilder;
import net.shortninja.staffplus.core.domain.staff.reporting.Report;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportService;
import net.shortninja.staffplusplus.reports.ReportFilters;

import java.util.List;

@IocBean
public class FindReportsViewBuilder {

    private final ReportService reportService;
    private final ReportItemBuilder reportItemBuilder;

    public FindReportsViewBuilder(ReportService reportService, ReportItemBuilder reportItemBuilder) {
        this.reportService = reportService;
        this.reportItemBuilder = reportItemBuilder;
    }

    public TubingGui buildGui(ReportFilters reportFilters, String currentAction, int page) {
        return new PagedGuiBuilder.Builder("Find reports")
            .addPagedItems(currentAction, getItems(page, reportFilters), reportItemBuilder::build, i -> getDetailAction(currentAction, i), page)
            .build();
    }

    private String getDetailAction(String currentAction, Report i) {
        return GuiActionBuilder.builder()
            .action("manage-reports/view/detail")
            .param("reportId", String.valueOf(i.getId()))
            .param("backAction", currentAction)
            .build();
    }

    public List<Report> getItems(int page, ReportFilters reportFilters) {
        return reportService.findReports(reportFilters, page * 45, 45);
    }
}