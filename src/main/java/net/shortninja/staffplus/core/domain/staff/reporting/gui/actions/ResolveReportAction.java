package net.shortninja.staffplus.core.domain.staff.reporting.gui.actions;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.staff.reporting.CloseReportRequest;
import net.shortninja.staffplus.core.domain.staff.reporting.ManageReportService;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import net.shortninja.staffplusplus.reports.ReportStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ResolveReportAction implements IAction {
    private static final String CANCEL = "cancel";
    private final Messages messages = StaffPlus.get().getIocContainer().get(Messages.class);

    private final SessionManagerImpl sessionManager = StaffPlus.get().getIocContainer().get(SessionManagerImpl.class);
    private final ManageReportService manageReportService = StaffPlus.get().getIocContainer().get(ManageReportService.class);
    private final Options options = StaffPlus.get().getIocContainer().get(Options.class);

    @Override
    public void click(Player player, ItemStack item, int slot, ClickType clickType) {

        int reportId = Integer.parseInt(StaffPlus.get().getIocContainer().get(IProtocolService.class).getVersionProtocol().getNbtString(item));
        if(options.reportConfiguration.isClosingReasonEnabled()) {
            messages.send(player, "&1===================================================", messages.prefixReports);
            messages.send(player, "&6       You have chosen to resolve this report", messages.prefixReports);
            messages.send(player, "&6Type your closing reason in chat to resolve the report", messages.prefixReports);
            messages.send(player, "&6      Type \"cancel\" to cancel closing the report ", messages.prefixReports);
            messages.send(player, "&1===================================================", messages.prefixReports);
            PlayerSession playerSession = sessionManager.get(player.getUniqueId());
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messages.send(player, "&CYou have cancelled rejecting this report", messages.prefixReports);
                    return;
                }
                manageReportService.closeReport(player, new CloseReportRequest(reportId, ReportStatus.RESOLVED, message));
            });
        } else {
            manageReportService.closeReport(player, new CloseReportRequest(reportId, ReportStatus.RESOLVED, null));
        }
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
