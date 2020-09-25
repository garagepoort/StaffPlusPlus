package net.shortninja.staffplus.staff.reporting.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.event.ReportStatus;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.reporting.CloseReportRequest;
import net.shortninja.staffplus.staff.reporting.ReportService;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RejectReportAction implements IAction {
    private static final String CANCEL = "cancel";
    private final Messages messages = IocContainer.getMessages();
    private final MessageCoordinator messageCoordinator = IocContainer.getMessage();
    private final SessionManager sessionManager = IocContainer.getSessionManager();
    private final ReportService reportService = IocContainer.getReportService();
    private final Options options = IocContainer.getOptions();

    @Override
    public void click(Player player, ItemStack item, int slot) {
        messageCoordinator.send(player, "======================================================", messages.prefixReports);
        messageCoordinator.send(player, "          You have chosen to reject this report", messages.prefixReports);
        messageCoordinator.send(player, " Type your closing reason in chat to reject the report", messages.prefixReports);
        messageCoordinator.send(player, "        Type \"cancel\" to cancel closing the report ", messages.prefixReports);
        messageCoordinator.send(player, "======================================================", messages.prefixReports);

        int reportId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
        if(options.reportConfiguration.isClosingReasonEnabled()) {
            PlayerSession playerSession = sessionManager.get(player.getUniqueId());
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messageCoordinator.send(player, "You have cancelled rejecting this report", messages.prefixReports);
                    return;
                }
                reportService.closeReport(player, new CloseReportRequest(reportId, ReportStatus.REJECTED, message));
            });
        } else {
            reportService.closeReport(player, new CloseReportRequest(reportId, ReportStatus.REJECTED, null));
        }
    }

    @Override
    public boolean shouldClose() {
        return true;
    }
}
