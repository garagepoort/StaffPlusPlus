package net.shortninja.staffplus.core.domain.staff.warn.appeals;

import be.garagepoort.mcioc.tubinggui.AsyncGui;
import be.garagepoort.mcioc.tubinggui.GuiAction;
import be.garagepoort.mcioc.tubinggui.GuiController;
import be.garagepoort.mcioc.tubinggui.GuiParam;
import be.garagepoort.mcioc.tubinggui.templates.GuiTemplate;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.appeals.Appeal;
import net.shortninja.staffplus.core.domain.staff.appeals.AppealService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static be.garagepoort.mcioc.tubinggui.AsyncGui.async;
import static be.garagepoort.mcioc.tubinggui.templates.GuiTemplate.template;
import static net.shortninja.staffplus.core.common.utils.Validator.validator;

@GuiController
public class WarningAppealGuiController {

    private static final String CANCEL = "cancel";
    private static final int PAGE_SIZE = 45;

    private final AppealService appealService;
    private final WarnService warnService;
    private final Messages messages;
    private final OnlineSessionsManager sessionManager;
    private final Options options;
    private final BukkitUtils bukkitUtils;
    private final WarningAppealConfiguration warningAppealConfiguration;
    private final ActionService actionService;
    private final PermissionHandler permission;
    private final PlayerManager playerManager;

    public WarningAppealGuiController(AppealService appealService,
                                      WarnService warnService,
                                      Messages messages,
                                      OnlineSessionsManager sessionManager,
                                      Options options,
                                      BukkitUtils bukkitUtils,
                                      WarningAppealConfiguration warningAppealConfiguration, ActionService actionService, PermissionHandler permission, PlayerManager playerManager) {
        this.appealService = appealService;
        this.warnService = warnService;
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.options = options;
        this.bukkitUtils = bukkitUtils;
        this.warningAppealConfiguration = warningAppealConfiguration;
        this.actionService = actionService;
        this.permission = permission;
        this.playerManager = playerManager;
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
            return template("gui/warnings/appeal-detail.ftl", params);
        });
    }

    @GuiAction("manage-warnings/view/appealed-warnings")
    public AsyncGui<GuiTemplate> appealedWarningsOverview(@GuiParam(value = "page", defaultValue = "0") int page) {
        return async(() -> {
            List<Warning> warnings = warnService.getAppealedWarnings(page * PAGE_SIZE, PAGE_SIZE);
            Map<String, Object> params = new HashMap<>();
            params.put("warnings", warnings);
            return template("gui/warnings/appealed-warnings-overview.ftl", params);
        });
    }

    @GuiAction("manage-warning-appeals/view/create/reason-select")
    public GuiTemplate getCreateAppealReasonSelectView(@GuiParam("warningId") int warningId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("action", "manage-warning-appeals/create?warningId=" + warningId);
        params.put("reasons", warningAppealConfiguration.appealReasons);
        return template("gui/appeals/appeal-reason-select.ftl", params);
    }

    @GuiAction("manage-warning-appeals/view/create/reason-chat")
    public void getCreateAppealReasonChatView(Player player, @GuiParam("warningId") int warningId) {
        SppPlayer sppPlayer = playerManager.getOnlinePlayer(player.getUniqueId())
            .orElseThrow((() -> new PlayerNotFoundException(player.getName())));

        Warning warning = warnService.getWarning(warningId);
        messages.send(player, messages.LONG_LINE, messages.prefixGeneral);
        messages.sendTranslation(player, "appeal-create-warning-selected", messages.prefixGeneral);
        messages.sendTranslation(player, "appeal-create-type-reason", messages.prefixGeneral);
        messages.sendTranslation(player, "appeal-create-cancel", messages.prefixGeneral);
        messages.send(player, messages.LONG_LINE, messages.prefixGeneral);

        OnlinePlayerSession playerSession = sessionManager.get(player);
        playerSession.setChatAction((player1, input) -> {
            if (input.equalsIgnoreCase(CANCEL)) {
                messages.sendTranslation(player, "appeal-create-cancelled", messages.prefixWarnings);
                return;
            }

            validator(player)
                .validateAnyPermission(warningAppealConfiguration.createAppealPermission)
                .validateNotEmpty(input, "Reason for appeal can not be empty");

            bukkitUtils.runTaskAsync(player, () -> appealService.addAppeal(sppPlayer, warning, input));
        });
    }

    @GuiAction("manage-warning-appeals/create")
    public void createAppeal(Player player, @GuiParam("warningId") int warningId, @GuiParam("reason") String reason) {
        validator(player)
            .validateAnyPermission(warningAppealConfiguration.createAppealPermission)
            .validateNotEmpty(reason, "Reason for appeal can not be empty");

        SppPlayer sppPlayer = playerManager.getOnlinePlayer(player.getUniqueId())
            .orElseThrow((() -> new PlayerNotFoundException(player.getName())));

        bukkitUtils.runTaskAsync(player, () -> {
            Warning warning = warnService.getWarning(warningId);
            appealService.addAppeal(sppPlayer, warning, reason);
        });
    }

    @GuiAction("manage-warning-appeals/approve")
    public void approveAppeal(Player player, @GuiParam("appealId") int appealId) {
        permission.validate(player, warningAppealConfiguration.approveAppealPermission);
        SppPlayer sppPlayer = playerManager.getOnlinePlayer(player.getUniqueId())
            .orElseThrow((() -> new PlayerNotFoundException(player.getName())));
        if (warningAppealConfiguration.resolveReasonEnabled) {
            messages.send(player, messages.LONG_LINE, messages.prefixWarnings);
            messages.sendTranslation(player, "appeal-approve-selected", messages.prefixWarnings);
            messages.sendTranslation(player, "appeal-approve-type-reason", messages.prefixWarnings);
            messages.sendTranslation(player, "appeal-approve-cancel", messages.prefixWarnings);
            messages.send(player, messages.LONG_LINE, messages.prefixWarnings);
            OnlinePlayerSession playerSession = sessionManager.get(player);
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messages.sendTranslation(player, "appeal-approve-cancelled", messages.prefixWarnings);
                    return;
                }
                bukkitUtils.runTaskAsync(player, () -> warnService.approveAppeal(sppPlayer, appealId, message));
            });
        } else {
            bukkitUtils.runTaskAsync(player, () -> warnService.approveAppeal(sppPlayer, appealId));
        }
    }

    @GuiAction("manage-warning-appeals/reject")
    public void rejectAppeal(Player player, @GuiParam("appealId") int appealId) {
        permission.validate(player, warningAppealConfiguration.rejectAppealPermission);
        SppPlayer sppPlayer = playerManager.getOnlinePlayer(player.getUniqueId())
            .orElseThrow((() -> new PlayerNotFoundException(player.getName())));
        if (warningAppealConfiguration.resolveReasonEnabled) {
            messages.send(player, messages.LONG_LINE, messages.prefixWarnings);
            messages.sendTranslation(player, "appeal-reject-selected", messages.prefixWarnings);
            messages.sendTranslation(player, "appeal-reject-type-reason", messages.prefixWarnings);
            messages.sendTranslation(player, "appeal-reject-cancel", messages.prefixWarnings);
            messages.send(player, messages.LONG_LINE, messages.prefixWarnings);
            OnlinePlayerSession playerSession = sessionManager.get(player);
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messages.sendTranslation(player, "appeal-reject-cancelled", messages.prefixWarnings);
                    return;
                }
                bukkitUtils.runTaskAsync(player, () -> warnService.rejectAppeal(sppPlayer, appealId, message));
            });
        } else {
            bukkitUtils.runTaskAsync(player, () -> warnService.rejectAppeal(sppPlayer, appealId));
        }
    }
}
