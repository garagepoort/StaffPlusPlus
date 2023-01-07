package net.shortninja.staffplus.core.domain.staff.reporting.queue.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.queue.QueueMessageListener;
import net.shortninja.staffplus.core.domain.staff.reporting.ManageReportService;
import net.shortninja.staffplus.core.domain.staff.reporting.queue.dto.ReportClosedQueueMessage;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;

@IocBean
@IocMultiProvider(QueueMessageListener.class)
public class ReportClosedListener implements QueueMessageListener<ReportClosedQueueMessage> {

    private final ManageReportService reportService;

    public ReportClosedListener(ManageReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public String handleMessage(ReportClosedQueueMessage message) {
        SppPlayer sppPlayer = new SppPlayer(message.getPlayerUuid(), message.getPlayerName(), Bukkit.getOfflinePlayer(message.getPlayerUuid()));
        reportService.closeReport(sppPlayer, message.getCloseReportRequest());
        return "Report with ID " + message.getCloseReportRequest().getReportId() + " has been closed";
    }

    @Override
    public String getType() {
        return "report/close";
    }

    @Override
    public Class getMessageClass() {
        return ReportClosedQueueMessage.class;
    }
}
