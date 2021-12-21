package net.shortninja.staffplus.core.domain.staff.mute.appeals;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.AsyncGui;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.templates.GuiTemplate;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.appeals.Appeal;
import net.shortninja.staffplus.core.domain.staff.appeals.AppealService;
import net.shortninja.staffplus.core.domain.staff.mute.Mute;
import net.shortninja.staffplus.core.domain.staff.mute.MuteService;
import net.shortninja.staffplusplus.appeals.AppealableType;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static be.garagepoort.mcioc.gui.AsyncGui.async;
import static be.garagepoort.mcioc.gui.templates.GuiTemplate.template;
import static net.shortninja.staffplus.core.common.utils.Validator.validator;

@IocBean
@GuiController
public class MuteAppealGuiController {

    private static final String CANCEL = "cancel";
    private static final int PAGE_SIZE = 45;

    private final AppealService appealService;
    private final MuteService muteService;
    private final Messages messages;
    private final OnlineSessionsManager sessionManager;
    private final BukkitUtils bukkitUtils;
    private final MuteAppealConfiguration muteAppealConfiguration;
    private final PermissionHandler permissionHandler;
    private final PlayerManager playerManager;

    public MuteAppealGuiController(AppealService appealService,
                                   MuteService muteService,
                                   Messages messages,
                                   OnlineSessionsManager sessionManager,
                                   BukkitUtils bukkitUtils,
                                   MuteAppealConfiguration muteAppealConfiguration, PermissionHandler permissionHandler, PlayerManager playerManager) {
        this.appealService = appealService;
        this.muteService = muteService;
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.bukkitUtils = bukkitUtils;
        this.muteAppealConfiguration = muteAppealConfiguration;
        this.permissionHandler = permissionHandler;
        this.playerManager = playerManager;
    }

    @GuiAction("manage-mute-appeals/view/detail")
    public AsyncGui<GuiTemplate> getAppealDetail(@GuiParam("appealId") int appealId) {
        return async(() -> {
            Appeal appeal = appealService.getAppeal(appealId);

            HashMap<String, Object> params = new HashMap<>();
            params.put("appeal", appeal);
            return template("gui/mutes/appeal-detail.ftl", params);
        });
    }

    @GuiAction("manage-mute-appeals/view/create/reason-select")
    public GuiTemplate getCreateAppealReasonSelectView(@GuiParam("muteId") int muteId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("action", "manage-mute-appeals/create?muteId=" + muteId);
        params.put("reasons", muteAppealConfiguration.appealReasons);
        return template("gui/appeals/appeal-reason-select.ftl", params);
    }

    @GuiAction("manage-mute-appeals/view/create/reason-chat")
    public void getCreateAppealReasonChatView(Player player, @GuiParam("muteId") int muteId) {
        Mute mute = muteService.getById(muteId);
        messages.send(player, "&1=====================================================", messages.prefixGeneral);
        messages.send(player, "&6         You have chosen to appeal this mute", messages.prefixGeneral);
        messages.send(player, "&6            Type your appeal reason in chat", messages.prefixGeneral);
        messages.send(player, "&6         Type \"cancel\" to cancel appealing ", messages.prefixGeneral);
        messages.send(player, "&1=====================================================", messages.prefixGeneral);

        OnlinePlayerSession playerSession = sessionManager.get(player);
        playerSession.setChatAction((player1, input) -> {
            if (input.equalsIgnoreCase(CANCEL)) {
                messages.send(player, "&CYou have cancelled your appeal", messages.prefixGeneral);
                return;
            }

            SppPlayer sppPlayer = playerManager.getOnlinePlayer(player.getUniqueId())
                .orElseThrow((() -> new PlayerNotFoundException(player.getName())));

            validator(player)
                .validateAnyPermission(muteAppealConfiguration.createAppealPermission)
                .validateNotEmpty(input, "Reason for appeal can not be empty");

            bukkitUtils.runTaskAsync(player, () -> appealService.addAppeal(sppPlayer, mute, input));
        });
    }

    @GuiAction("manage-mute-appeals/create")
    public void createAppeal(Player player, @GuiParam("muteId") int muteId, @GuiParam("reason") String reason) {
        validator(player)
            .validateAnyPermission(muteAppealConfiguration.createAppealPermission)
            .validateNotEmpty(reason, "Reason for appeal can not be empty");

        SppPlayer sppPlayer = playerManager.getOnlinePlayer(player.getUniqueId())
            .orElseThrow((() -> new PlayerNotFoundException(player.getName())));

        bukkitUtils.runTaskAsync(player, () -> {
            Mute mute = muteService.getById(muteId);
            appealService.addAppeal(sppPlayer, mute, reason);
        });
    }

    @GuiAction("manage-mutes/view/appealed-mutes")
    public AsyncGui<GuiTemplate> appealedMutesOverview(@GuiParam(value = "page", defaultValue = "0") int page) {
        return async(() -> {
            List<Mute> mutes = muteService.getAppealedMutes(page * PAGE_SIZE, PAGE_SIZE);
            Map<String, Object> params = new HashMap<>();
            params.put("mutes", mutes);
            return template("gui/mutes/appealed-mutes-overview.ftl", params);
        });
    }

    @GuiAction("manage-mute-appeals/approve")
    public void approveAppeal(Player player, @GuiParam("appealId") int appealId) {
        permissionHandler.validate(player, muteAppealConfiguration.approveAppealPermission);
        SppPlayer sppPlayer = playerManager.getOnlinePlayer(player.getUniqueId())
            .orElseThrow((() -> new PlayerNotFoundException(player.getName())));
        if (muteAppealConfiguration.resolveReasonEnabled) {
            messages.send(player, "&1===================================================", messages.prefixGeneral);
            messages.send(player, "&6       You have chosen to approve this appeal", messages.prefixGeneral);
            messages.send(player, "&6Type your closing reason in chat to approve the appeal", messages.prefixGeneral);
            messages.send(player, "&6      Type \"cancel\" to cancel approving the appeal ", messages.prefixGeneral);
            messages.send(player, "&1===================================================", messages.prefixGeneral);
            OnlinePlayerSession playerSession = sessionManager.get(player);
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messages.send(player, "&CYou have cancelled approving this appeal", messages.prefixGeneral);
                    return;
                }
                bukkitUtils.runTaskAsync(player, () -> appealService.approveAppeal(sppPlayer, appealId, message, AppealableType.MUTE));
            });
        } else {
            bukkitUtils.runTaskAsync(player, () -> appealService.approveAppeal(sppPlayer, appealId, AppealableType.MUTE));
        }
    }

    @GuiAction("manage-mute-appeals/reject")
    public void rejectAppeal(Player player, @GuiParam("appealId") int appealId) {
        permissionHandler.validate(player, muteAppealConfiguration.rejectAppealPermission);
        SppPlayer sppPlayer = playerManager.getOnlinePlayer(player.getUniqueId())
            .orElseThrow((() -> new PlayerNotFoundException(player.getName())));
        if (muteAppealConfiguration.resolveReasonEnabled) {
            messages.send(player, "&1==================================================", messages.prefixGeneral);
            messages.send(player, "&6        You have chosen to reject this appeal", messages.prefixGeneral);
            messages.send(player, "&6Type your closing reason in chat to reject the appeal", messages.prefixGeneral);
            messages.send(player, "&6        Type \"cancel\" to cancel closing the appeal ", messages.prefixGeneral);
            messages.send(player, "&1==================================================", messages.prefixGeneral);
            OnlinePlayerSession playerSession = sessionManager.get(player);
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messages.send(player, "&CYou have cancelled rejecting this appeal", messages.prefixGeneral);
                    return;
                }
                bukkitUtils.runTaskAsync(player, () -> appealService.rejectAppeal(sppPlayer, appealId, message, AppealableType.MUTE));
            });
        } else {
            bukkitUtils.runTaskAsync(player, () -> appealService.rejectAppeal(sppPlayer, appealId, AppealableType.MUTE));
        }
    }
}
