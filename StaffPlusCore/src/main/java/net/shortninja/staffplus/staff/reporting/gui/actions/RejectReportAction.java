package net.shortninja.staffplus.staff.reporting.gui.actions;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplusplus.reports.ReportStatus;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.staff.reporting.CloseReportRequest;
import net.shortninja.staffplus.staff.reporting.ManageReportService;
import net.shortninja.staffplus.common.IAction;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RejectReportAction implements IAction {
    private static final String CANCEL = "cancel";
    private final Messages messages = IocContainer.getMessages();
    private final MessageCoordinator messageCoordinator = IocContainer.getMessage();
    private final SessionManager sessionManager = IocContainer.getSessionManager();
    private final ManageReportService manageReportService = IocContainer.getManageReportService();
    private final Options options = IocContainer.getOptions();

    @Override
    public void click(Player player, ItemStack item, int slot) {

        int reportId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
        if (options.reportConfiguration.isClosingReasonEnabled()) {
            messageCoordinator.send(player, "&1==================================================", messages.prefixReports);
            messageCoordinator.send(player, "&6        You have chosen to reject this report", messages.prefixReports);
            messageCoordinator.send(player, "&6Type your closing reason in chat to reject the report", messages.prefixReports);
            messageCoordinator.send(player, "&6        Type \"cancel\" to cancel closing the report ", messages.prefixReports);
            messageCoordinator.send(player, "&1==================================================", messages.prefixReports);
            PlayerSession playerSession = sessionManager.get(player.getUniqueId());
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messageCoordinator.send(player, "&CYou have cancelled rejecting this report", messages.prefixReports);
                    return;
                }
                manageReportService.closeReport(player, new CloseReportRequest(reportId, ReportStatus.REJECTED, message));
            });
        } else {
            manageReportService.closeReport(player, new CloseReportRequest(reportId, ReportStatus.REJECTED, null));
        }
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
