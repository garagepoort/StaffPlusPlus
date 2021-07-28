package net.shortninja.staffplus.core.domain.staff.warn.appeals.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.Appeal;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.AppealService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import org.bukkit.entity.Player;

@IocBean
@GuiController
public class WarningAppealGuiController {

    private static final String CANCEL = "cancel";

    private final AppealReasonSelectViewBuilder appealReasonSelectViewBuilder;
    private final ManageAppealViewBuilder manageAppealViewBuilder;
    private final AppealService appealService;
    private final WarnService warnService;
    private final Messages messages;
    private final SessionManagerImpl sessionManager;
    private final Options options;

    public WarningAppealGuiController(AppealReasonSelectViewBuilder appealReasonSelectViewBuilder, ManageAppealViewBuilder manageAppealViewBuilder, AppealService appealService, WarnService warnService, Messages messages, SessionManagerImpl sessionManager, Options options) {
        this.appealReasonSelectViewBuilder = appealReasonSelectViewBuilder;
        this.manageAppealViewBuilder = manageAppealViewBuilder;
        this.appealService = appealService;
        this.warnService = warnService;
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.options = options;
    }

    @GuiAction("manage-warning-appeals/view/detail")
    public TubingGui getAppealDetail(Player player,
                                     @GuiParam("appealId") int appealId,
                                     @GuiParam("backAction") String backAction) {
        Appeal appeal = appealService.getAppeal(appealId);
        Warning warning = warnService.getWarning(appeal.getAppealableId());
        return manageAppealViewBuilder.buildGui(player, appeal, warning, backAction);
    }

    @GuiAction("manage-warning-appeals/view/create/reason-select")
    public TubingGui getCreateAppealReasonSelectView(@GuiParam("warningId") int warningId) {
        return appealReasonSelectViewBuilder.buildGui(warningId);
    }

    @GuiAction("manage-warning-appeals/view/create/reason-chat")
    public void getCreateAppealReasonChatView(Player player, @GuiParam("warningId") int warningId) {
        Warning warning = warnService.getWarning(warningId);
        messages.send(player, "&1=====================================================", messages.prefixGeneral);
        messages.send(player, "&6         You have chosen to appeal this warning", messages.prefixGeneral);
        messages.send(player, "&6            Type your appeal reason in chat", messages.prefixGeneral);
        messages.send(player, "&6         Type \"cancel\" to cancel appealing ", messages.prefixGeneral);
        messages.send(player, "&1=====================================================", messages.prefixGeneral);

        PlayerSession playerSession = sessionManager.get(player.getUniqueId());
        playerSession.setChatAction((player1, input) -> {
            if (input.equalsIgnoreCase(CANCEL)) {
                messages.send(player, "&CYou have cancelled your appeal", messages.prefixWarnings);
                return;
            }

            appealService.addAppeal(player, warning, input);
        });
    }

    @GuiAction("manage-warning-appeals/create")
    public void createAppeal(Player player, @GuiParam("warningId") int warningId, @GuiParam("reason") String reason) {
        Warning warning = warnService.getWarning(warningId);
        appealService.addAppeal(player, warning, reason);
    }

    @GuiAction("manage-warning-appeals/approve")
    public void approveAppeal(Player player, @GuiParam("appealId") int appealId) {
        if (options.appealConfiguration.resolveReasonEnabled) {
            messages.send(player, "&1===================================================", messages.prefixWarnings);
            messages.send(player, "&6       You have chosen to approve this appeal", messages.prefixWarnings);
            messages.send(player, "&6Type your closing reason in chat to approve the appeal", messages.prefixWarnings);
            messages.send(player, "&6      Type \"cancel\" to cancel approving the appeal ", messages.prefixWarnings);
            messages.send(player, "&1===================================================", messages.prefixWarnings);
            PlayerSession playerSession = sessionManager.get(player.getUniqueId());
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messages.send(player, "&CYou have cancelled approving this appeal", messages.prefixWarnings);
                    return;
                }
                appealService.approveAppeal(player, appealId, message);
            });
        } else {
            appealService.approveAppeal(player, appealId);
        }
    }

    @GuiAction("manage-warning-appeals/reject")
    public void rejectAppeal(Player player, @GuiParam("appealId") int appealId) {
        if (options.appealConfiguration.resolveReasonEnabled) {
            messages.send(player, "&1==================================================", messages.prefixWarnings);
            messages.send(player, "&6        You have chosen to reject this appeal", messages.prefixWarnings);
            messages.send(player, "&6Type your closing reason in chat to reject the appeal", messages.prefixWarnings);
            messages.send(player, "&6        Type \"cancel\" to cancel closing the appeal ", messages.prefixWarnings);
            messages.send(player, "&1==================================================", messages.prefixWarnings);
            PlayerSession playerSession = sessionManager.get(player.getUniqueId());
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messages.send(player, "&CYou have cancelled rejecting this appeal", messages.prefixWarnings);
                    return;
                }
                appealService.rejectAppeal(player, appealId, message);
            });
        } else {
            appealService.rejectAppeal(player, appealId);
        }
    }


}
