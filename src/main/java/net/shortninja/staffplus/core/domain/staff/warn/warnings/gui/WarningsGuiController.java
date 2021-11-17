package net.shortninja.staffplus.core.domain.staff.warn.warnings.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.*;
import be.garagepoort.mcioc.gui.model.TubingGui;
import be.garagepoort.mcioc.gui.templates.GuiTemplate;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.examine.gui.SeverityLevelSelectViewBuilder;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningSeverityConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static be.garagepoort.mcioc.gui.AsyncGui.async;
import static be.garagepoort.mcioc.gui.templates.GuiTemplate.template;

@IocBean
@GuiController
public class WarningsGuiController {

    private static final String CANCEL = "cancel";
    private static final String NONE = "none";

    private final ManageWarningsViewBuilder manageWarningsViewBuilder;
    private final MyWarningsViewBuilder myWarningsViewBuilder;
    private final ManageAppealedWarningsViewBuilder manageAppealedWarningsViewBuilder;
    private final SeverityLevelSelectViewBuilder severityLevelSelectViewBuilder;
    private final PlayerManager playerManager;
    private final WarnService warnService;
    private final OnlineSessionsManager sessionManager;
    private final Options options;
    private final Messages messages;
    private final BukkitUtils bukkitUtils;
    private final ActionService actionService;

    public WarningsGuiController(ManageWarningsViewBuilder manageWarningsViewBuilder,
                                 MyWarningsViewBuilder myWarningsViewBuilder,
                                 ManageAppealedWarningsViewBuilder manageAppealedWarningsViewBuilder,
                                 SeverityLevelSelectViewBuilder severityLevelSelectViewBuilder, PlayerManager playerManager,
                                 WarnService warnService, OnlineSessionsManager sessionManager, Options options, Messages messages, BukkitUtils bukkitUtils, ActionService actionService) {
        this.manageWarningsViewBuilder = manageWarningsViewBuilder;
        this.myWarningsViewBuilder = myWarningsViewBuilder;
        this.manageAppealedWarningsViewBuilder = manageAppealedWarningsViewBuilder;
        this.severityLevelSelectViewBuilder = severityLevelSelectViewBuilder;
        this.playerManager = playerManager;
        this.warnService = warnService;
        this.sessionManager = sessionManager;
        this.options = options;
        this.messages = messages;
        this.bukkitUtils = bukkitUtils;
        this.actionService = actionService;
    }

    @GuiAction("manage-warnings/view/overview")
    public TubingGui warningsOverview(@GuiParam("targetPlayerName") String targetPlayerName,
                                      @GuiParam("backAction") String backAction,
                                      @CurrentAction String currentAction,
                                      @GuiParam(value = "page", defaultValue = "0") int page) {
        if (StringUtils.isNotBlank(targetPlayerName)) {
            SppPlayer target = playerManager.getOnOrOfflinePlayer(targetPlayerName).orElseThrow((() -> new PlayerNotFoundException(targetPlayerName)));
            return manageWarningsViewBuilder.buildGui(target, currentAction, page, backAction);
        }
        return manageWarningsViewBuilder.buildGui(null, currentAction, page, backAction);
    }

    @GuiAction("manage-warnings/view/my-warnings")
    public TubingGui myWarningsOverview(Player player,
                                        @CurrentAction String currentAction,
                                        @GuiParam(value = "page", defaultValue = "0") int page) {
        return myWarningsViewBuilder.buildGui(player, currentAction, page);
    }

    @GuiAction("manage-warnings/view/select-severity")
    public TubingGui selectSeverity(@GuiParam("targetPlayerName") String targetPlayerName,
                                    @GuiParam("backAction") String backAction) {
        SppPlayer target = playerManager.getOnOrOfflinePlayer(targetPlayerName).orElseThrow((() -> new PlayerNotFoundException(targetPlayerName)));
        return severityLevelSelectViewBuilder.buildGui(target, backAction);
    }

    @GuiAction("manage-warnings/view/appealed-warnings")
    public TubingGui appealedWarningsOverview(@CurrentAction String currentAction,
                                              @GuiParam(value = "page", defaultValue = "0") int page) {
        return manageAppealedWarningsViewBuilder.buildGui(currentAction, page);
    }

    @GuiAction("manage-warnings/view/detail")
    public AsyncGui<GuiTemplate> warningDetail(@GuiParam("warningId") int warningId) {
        return async(() -> {
            Warning warning = warnService.getWarning(warningId);
            Map<String, Object> params = new HashMap<>();
            params.put("warning", warning);
            params.put("rollbackCommands", actionService.getActions(warning).stream()
                .filter(s -> s.isExecuted() && s.isRollbackable() && !s.isRollbacked())
                .map(s -> s.getRollbackCommand().get().getCommand())
                .collect(Collectors.toList()));

            return template("gui/warnings/warning-detail.ftl", params);
        });
    }

    @GuiAction("manage-warnings/delete")
    public String deleteWarning(@GuiParam("warningId") int warningId,
                                @GuiParam("backAction") String backAction) {
        warnService.removeWarning(warningId);
        return backAction;
    }

    @GuiAction("manage-warnings/expire")
    public void expireWarning(Player player, @GuiParam("warningId") int warningId) {
        bukkitUtils.runTaskAsync(player, () -> warnService.expireWarning(warningId));
    }

    @GuiAction("manage-warnings/warn")
    public void warnPlayer(Player player,
                           @GuiParam("severity") String severityLevel,
                           @GuiParam("targetPlayerName") String targetPlayerName) {
        OnlinePlayerSession playerSession = sessionManager.get(player);

        WarningSeverityConfiguration severityConfiguration = options.warningConfiguration.getSeverityConfiguration(severityLevel)
            .orElseThrow(() -> new BusinessException("&CNo severity configuration found for level [" + severityLevel + "]"));

        SppPlayer onOrOfflinePlayer = playerManager.getOnOrOfflinePlayer(targetPlayerName).orElseThrow(() -> new PlayerNotFoundException(targetPlayerName));

        if (!severityConfiguration.isReasonSettable()) {
            warnService.sendWarning(player, onOrOfflinePlayer, null, severityConfiguration);
        } else {
            promptReasonInput(player, playerSession, onOrOfflinePlayer, severityConfiguration);
        }

    }

    private void promptReasonInput(Player player, OnlinePlayerSession playerSession, SppPlayer onOrOfflinePlayer, WarningSeverityConfiguration severityConfiguration) {
        boolean defaultReasonSettable = severityConfiguration.getReason().isPresent() && severityConfiguration.isReasonOverwriteEnabled();

        messages.send(player, "&1=====================================================", messages.prefixGeneral);
        messages.send(player, "&6         You have chosen to warn this player", messages.prefixGeneral);
        messages.send(player, "&6Type your reason for warning this player in chat", messages.prefixGeneral);
        if (defaultReasonSettable) {
            messages.send(player, "&6Type \"none\" to use the default reason", messages.prefixGeneral);
        }
        messages.send(player, "&6        Type \"cancel\" to cancel the warning ", messages.prefixGeneral);
        messages.send(player, "&1=====================================================", messages.prefixGeneral);


        playerSession.setChatAction((player1, input) -> {
            if (input.equalsIgnoreCase(CANCEL)) {
                messages.send(player, "&CYou have cancelled warning this player", messages.prefixWarnings);
                return;
            }
            if (input.equalsIgnoreCase(NONE) && defaultReasonSettable) {
                input = null;
            }

            warnService.sendWarning(player1, onOrOfflinePlayer, input, severityConfiguration);
            messages.send(player1, messages.inputAccepted, messages.prefixGeneral);
        });
    }
}
