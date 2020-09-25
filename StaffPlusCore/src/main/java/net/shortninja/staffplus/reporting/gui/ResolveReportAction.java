package net.shortninja.staffplus.reporting.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.event.ReportStatus;
import net.shortninja.staffplus.player.PlayerSession;
import net.shortninja.staffplus.reporting.CloseReportRequest;
import net.shortninja.staffplus.reporting.ReportService;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ResolveReportAction implements IAction {
    private static final String CANCEL = "cancel";
    private final Messages messages = IocContainer.getMessages();
    private final MessageCoordinator messageCoordinator = IocContainer.getMessage();
    private final SessionManager sessionManager = IocContainer.getSessionManager();
    private final ReportService reportService = IocContainer.getReportService();

    @Override
    public void click(Player player, ItemStack item, int slot) {
        messageCoordinator.send(player, "======================================================", messages.prefixReports);
        messageCoordinator.send(player, "         You have chosen to resolve this report", messages.prefixReports);
        messageCoordinator.send(player, " Type your closing reason in chat to resolve the report", messages.prefixReports);
        messageCoordinator.send(player, "        Type \"cancel\" to cancel closing the report ", messages.prefixReports);
        messageCoordinator.send(player, "======================================================", messages.prefixReports);

        int reportId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
        PlayerSession playerSession = sessionManager.get(player.getUniqueId());
        playerSession.setChatAction((player1, message) -> {
            if (message.equalsIgnoreCase(CANCEL)) {
                messageCoordinator.send(player, "You have cancelled rejecting this report", messages.prefixReports);
            }
            reportService.closeReport(player, new CloseReportRequest(reportId, ReportStatus.RESOLVED, message));
        });
    }

    @Override
    public boolean shouldClose() {
        return true;
    }
}
