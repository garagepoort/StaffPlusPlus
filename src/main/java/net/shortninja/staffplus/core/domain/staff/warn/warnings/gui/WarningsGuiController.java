package net.shortninja.staffplus.core.domain.staff.warn.warnings.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.AsyncGui;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.templates.GuiTemplate;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningSeverityConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static be.garagepoort.mcioc.gui.AsyncGui.async;
import static be.garagepoort.mcioc.gui.templates.GuiTemplate.template;

@IocBean
@GuiController
public class WarningsGuiController {

    private static final String CANCEL = "cancel";
    private static final String NONE = "none";
    private static final int PAGE_SIZE = 45;

    private final ManageAppealedWarningsViewBuilder manageAppealedWarningsViewBuilder;
    private final PlayerManager playerManager;
    private final WarnService warnService;
    private final OnlineSessionsManager sessionManager;
    private final WarningConfiguration warningConfiguration;
    private final Messages messages;
    private final BukkitUtils bukkitUtils;
    private final ActionService actionService;

    public WarningsGuiController(ManageAppealedWarningsViewBuilder manageAppealedWarningsViewBuilder,
                                 PlayerManager playerManager,
                                 WarnService warnService,
                                 OnlineSessionsManager sessionManager,
                                 WarningConfiguration warningConfiguration,
                                 Messages messages,
                                 BukkitUtils bukkitUtils,
                                 ActionService actionService) {
        this.manageAppealedWarningsViewBuilder = manageAppealedWarningsViewBuilder;
        this.playerManager = playerManager;
        this.warnService = warnService;
        this.sessionManager = sessionManager;
        this.warningConfiguration = warningConfiguration;
        this.messages = messages;
        this.bukkitUtils = bukkitUtils;
        this.actionService = actionService;
    }

    @GuiAction("manage-warnings/view/overview")
    public AsyncGui<GuiTemplate> warningsOverview(@GuiParam("targetPlayerName") String targetPlayerName,
                                                  @GuiParam(value = "page", defaultValue = "0") int page) {
        SppPlayer target = null;
        if (StringUtils.isNotBlank(targetPlayerName)) {
            target = playerManager.getOnOrOfflinePlayer(targetPlayerName).orElseThrow((() -> new PlayerNotFoundException(targetPlayerName)));
        }
        SppPlayer finalTarget = target;
        return async(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("warnings", getWarnings(finalTarget, page));
            return template("gui/warnings/warnings-overview.ftl", params);
        });
    }

    private List<Warning> getWarnings(SppPlayer target, int page) {
        if (target == null) {
            return warnService.getAllWarnings(page * PAGE_SIZE, PAGE_SIZE, true);
        }
        return warnService.getWarnings(target.getId(), page * PAGE_SIZE, PAGE_SIZE, true);
    }

    @GuiAction("manage-warnings/view/my-warnings")
    public AsyncGui<GuiTemplate> myWarningsOverview(Player player,
                                                    @GuiParam(value = "page", defaultValue = "0") int page) {
        return async(() -> {
            List<Warning> warnings = warnService.getWarnings(player.getUniqueId(), page * PAGE_SIZE, PAGE_SIZE, false);
            Map<String, Object> params = new HashMap<>();
            params.put("warnings", warnings);
            return template("gui/warnings/my-warnings-overview.ftl", params);
        });
    }

    @GuiAction("manage-warnings/view/select-severity")
    public GuiTemplate selectSeverity(@GuiParam("targetPlayerName") String targetPlayerName) {
        SppPlayer target = playerManager.getOnOrOfflinePlayer(targetPlayerName).orElseThrow((() -> new PlayerNotFoundException(targetPlayerName)));
        Map<String, Object> params = new HashMap<>();
        params.put("target", target);
        params.put("severityLevels", warningConfiguration.getSeverityLevels());

        return template("gui/warnings/severity-selection.ftl", params);
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

        WarningSeverityConfiguration severityConfiguration = warningConfiguration.getSeverityConfiguration(severityLevel)
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
