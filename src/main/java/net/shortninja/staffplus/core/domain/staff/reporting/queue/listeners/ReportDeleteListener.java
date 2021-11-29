package net.shortninja.staffplus.core.domain.staff.reporting.queue.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.queue.QueueMessageListener;
import net.shortninja.staffplus.core.domain.staff.reporting.ManageReportService;
import net.shortninja.staffplus.core.domain.staff.reporting.queue.dto.ReportQueueMessage;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;

@IocBean
@IocMultiProvider(QueueMessageListener.class)
public class ReportDeleteListener implements QueueMessageListener<ReportQueueMessage> {

    private final ManageReportService reportService;

    public ReportDeleteListener(ManageReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public String handleMessage(ReportQueueMessage message) {
        SppPlayer sppPlayer = new SppPlayer(message.getPlayerUuid(), message.getPlayerName(), Bukkit.getOfflinePlayer(message.getPlayerUuid()));
        reportService.deleteReport(sppPlayer, message.getReportId());
        return "Report with ID " + message.getReportId() + " has been deleted";
    }

    @Override
    public String getType() {
        return "report/delete";
    }

    @Override
    public Class getMessageClass() {
        return ReportQueueMessage.class;
    }
}
