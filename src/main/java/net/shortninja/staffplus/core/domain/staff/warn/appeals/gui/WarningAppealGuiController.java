package net.shortninja.staffplus.core.domain.staff.warn.appeals.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.AsyncGui;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.templates.GuiTemplate;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.Appeal;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.AppealService;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.config.WarningAppealConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static be.garagepoort.mcioc.gui.AsyncGui.async;

@IocBean
@GuiController
public class WarningAppealGuiController {

    private static final String CANCEL = "cancel";

    private final AppealService appealService;
    private final WarnService warnService;
    private final Messages messages;
    private final OnlineSessionsManager sessionManager;
    private final Options options;
    private final BukkitUtils bukkitUtils;
    private final WarningAppealConfiguration warningAppealConfiguration;
    private final ActionService actionService;

    public WarningAppealGuiController(AppealService appealService,
                                      WarnService warnService,
                                      Messages messages,
                                      OnlineSessionsManager sessionManager,
                                      Options options,
                                      BukkitUtils bukkitUtils,
                                      WarningAppealConfiguration warningAppealConfiguration, ActionService actionService) {
        this.appealService = appealService;
        this.warnService = warnService;
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.options = options;
        this.bukkitUtils = bukkitUtils;
        this.warningAppealConfiguration = warningAppealConfiguration;
        this.actionService = actionService;
    }

    @GuiAction("manage-warning-appeals/view/detail")
    public AsyncGui<GuiTemplate> getAppealDetail(@GuiParam("appealId") int appealId) {
        return async(() -> {
            Appeal appeal = appealService.getAppeal(appealId);
            Warning warning = warnService.getWarning(appeal.getAppealableId());
            List<String> rollbackCommands = actionService.getActions(warning).stream()
                .filter(s -> s.isExecuted() && s.isRollbackable() && !s.isRollbacked())
                .map(s -> s.getRollbackCommand().get().getCommand())
                .collect(Collectors.toList());

            HashMap<String, Object> params = new HashMap<>();
            params.put("appeal", appeal);
            params.put("rollbackCommands", rollbackCommands);
            return GuiTemplate.template("gui/warnings/appeal-detail.ftl", params);
        });
    }

    @GuiAction("manage-warning-appeals/view/create/reason-select")
    public GuiTemplate getCreateAppealReasonSelectView(@GuiParam("warningId") int warningId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("action", "manage-warning-appeals/create?warningId=" + warningId);
        params.put("reasons", warningAppealConfiguration.appealReasons);
        return GuiTemplate.template("gui/appeals/appeal-reason-select.ftl", params);
    }

    @GuiAction("manage-warning-appeals/view/create/reason-chat")
    public void getCreateAppealReasonChatView(Player player, @GuiParam("warningId") int warningId) {
        Warning warning = warnService.getWarning(warningId);
        messages.send(player, "&1=====================================================", messages.prefixGeneral);
        messages.send(player, "&6         You have chosen to appeal this warning", messages.prefixGeneral);
        messages.send(player, "&6            Type your appeal reason in chat", messages.prefixGeneral);
        messages.send(player, "&6         Type \"cancel\" to cancel appealing ", messages.prefixGeneral);
        messages.send(player, "&1=====================================================", messages.prefixGeneral);

        OnlinePlayerSession playerSession = sessionManager.get(player);
        playerSession.setChatAction((player1, input) -> {
            if (input.equalsIgnoreCase(CANCEL)) {
                messages.send(player, "&CYou have cancelled your appeal", messages.prefixWarnings);
                return;
            }

            bukkitUtils.runTaskAsync(player, () -> appealService.addAppeal(player, warning, input));
        });
    }

    @GuiAction("manage-warning-appeals/create")
    public void createAppeal(Player player, @GuiParam("warningId") int warningId, @GuiParam("reason") String reason) {
        bukkitUtils.runTaskAsync(player, () -> {
            Warning warning = warnService.getWarning(warningId);
            appealService.addAppeal(player, warning, reason);
        });
    }

    @GuiAction("manage-warning-appeals/approve")
    public void approveAppeal(Player player, @GuiParam("appealId") int appealId) {
        if (options.warningAppealConfiguration.resolveReasonEnabled) {
            messages.send(player, "&1===================================================", messages.prefixWarnings);
            messages.send(player, "&6       You have chosen to approve this appeal", messages.prefixWarnings);
            messages.send(player, "&6Type your closing reason in chat to approve the appeal", messages.prefixWarnings);
            messages.send(player, "&6      Type \"cancel\" to cancel approving the appeal ", messages.prefixWarnings);
            messages.send(player, "&1===================================================", messages.prefixWarnings);
            OnlinePlayerSession playerSession = sessionManager.get(player);
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messages.send(player, "&CYou have cancelled approving this appeal", messages.prefixWarnings);
                    return;
                }
                bukkitUtils.runTaskAsync(player, () -> warnService.approveAppeal(player, appealId, message));
            });
        } else {
            bukkitUtils.runTaskAsync(player, () -> warnService.approveAppeal(player, appealId));
        }
    }

    @GuiAction("manage-warning-appeals/reject")
    public void rejectAppeal(Player player, @GuiParam("appealId") int appealId) {
        if (options.warningAppealConfiguration.resolveReasonEnabled) {
            messages.send(player, "&1==================================================", messages.prefixWarnings);
            messages.send(player, "&6        You have chosen to reject this appeal", messages.prefixWarnings);
            messages.send(player, "&6Type your closing reason in chat to reject the appeal", messages.prefixWarnings);
            messages.send(player, "&6        Type \"cancel\" to cancel closing the appeal ", messages.prefixWarnings);
            messages.send(player, "&1==================================================", messages.prefixWarnings);
            OnlinePlayerSession playerSession = sessionManager.get(player);
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messages.send(player, "&CYou have cancelled rejecting this appeal", messages.prefixWarnings);
                    return;
                }
                bukkitUtils.runTaskAsync(player, () -> warnService.rejectAppeal(player, appealId, message));
            });
        } else {
            bukkitUtils.runTaskAsync(player, () -> warnService.rejectAppeal(player, appealId));
        }
    }


}
