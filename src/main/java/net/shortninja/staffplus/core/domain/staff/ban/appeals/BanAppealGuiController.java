package net.shortninja.staffplus.core.domain.staff.ban.appeals;

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
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.Ban;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanService;
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
public class BanAppealGuiController {

    private static final String CANCEL = "cancel";
    private static final int PAGE_SIZE = 45;

    private final AppealService appealService;
    private final BanService banService;
    private final Messages messages;
    private final OnlineSessionsManager sessionManager;
    private final BukkitUtils bukkitUtils;
    private final BanAppealConfiguration banAppealConfiguration;
    private final PermissionHandler permissionHandler;
    private final PlayerManager playerManager;

    public BanAppealGuiController(AppealService appealService,
                                  BanService banService,
                                  Messages messages,
                                  OnlineSessionsManager sessionManager,
                                  BukkitUtils bukkitUtils,
                                  BanAppealConfiguration banAppealConfiguration, PermissionHandler permissionHandler, PlayerManager playerManager) {
        this.appealService = appealService;
        this.banService = banService;
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.bukkitUtils = bukkitUtils;
        this.banAppealConfiguration = banAppealConfiguration;
        this.permissionHandler = permissionHandler;
        this.playerManager = playerManager;
    }

    @GuiAction("manage-ban-appeals/view/detail")
    public AsyncGui<GuiTemplate> getAppealDetail(@GuiParam("appealId") int appealId) {
        return async(() -> {
            Appeal appeal = appealService.getAppeal(appealId);

            HashMap<String, Object> params = new HashMap<>();
            params.put("appeal", appeal);
            return template("gui/bans/appeal-detail.ftl", params);
        });
    }

    @GuiAction("manage-ban-appeals/view/create/reason-select")
    public GuiTemplate getCreateAppealReasonSelectView(@GuiParam("banId") int banId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("action", "manage-ban-appeals/create?banId=" + banId);
        params.put("reasons", banAppealConfiguration.appealReasons);
        return template("gui/appeals/appeal-reason-select.ftl", params);
    }

    @GuiAction("manage-ban-appeals/view/create/reason-chat")
    public void getCreateAppealReasonChatView(Player player, @GuiParam("banId") int banId) {
        Ban ban = banService.getById(banId);
        messages.send(player, "&1=====================================================", messages.prefixGeneral);
        messages.send(player, "&6         You have chosen to appeal this ban", messages.prefixGeneral);
        messages.send(player, "&6            Type your appeal reason in chat", messages.prefixGeneral);
        messages.send(player, "&6         Type \"cancel\" to cancel appealing ", messages.prefixGeneral);
        messages.send(player, "&1=====================================================", messages.prefixGeneral);

        OnlinePlayerSession playerSession = sessionManager.get(player);
        playerSession.setChatAction((player1, input) -> {
            if (input.equalsIgnoreCase(CANCEL)) {
                messages.send(player, "&CYou have cancelled your appeal", messages.prefixBans);
                return;
            }

            SppPlayer sppPlayer = playerManager.getOnlinePlayer(player.getUniqueId())
                .orElseThrow((() -> new PlayerNotFoundException(player.getName())));

            validator(player)
                .validateAnyPermission(banAppealConfiguration.createAppealPermission)
                .validateNotEmpty(input, "Reason for appeal can not be empty");

            bukkitUtils.runTaskAsync(player, () -> appealService.addAppeal(sppPlayer, ban, input));
        });
    }

    @GuiAction("manage-ban-appeals/create")
    public void createAppeal(Player player, @GuiParam("banId") int banId, @GuiParam("reason") String reason) {
        validator(player)
            .validateAnyPermission(banAppealConfiguration.createAppealPermission)
            .validateNotEmpty(reason, "Reason for appeal can not be empty");

        SppPlayer sppPlayer = playerManager.getOnlinePlayer(player.getUniqueId())
            .orElseThrow((() -> new PlayerNotFoundException(player.getName())));

        bukkitUtils.runTaskAsync(player, () -> {
            Ban ban = banService.getById(banId);
            appealService.addAppeal(sppPlayer, ban, reason);
        });
    }

    @GuiAction("manage-bans/view/appealed-bans")
    public AsyncGui<GuiTemplate> appealedBansOverview(@GuiParam(value = "page", defaultValue = "0") int page) {
        return async(() -> {
            List<Ban> bans = banService.getAppealedBans(page * PAGE_SIZE, PAGE_SIZE);
            Map<String, Object> params = new HashMap<>();
            params.put("bans", bans);
            return template("gui/bans/appealed-bans-overview.ftl", params);
        });
    }

    @GuiAction("manage-ban-appeals/approve")
    public void approveAppeal(Player player, @GuiParam("appealId") int appealId) {
        permissionHandler.validate(player, banAppealConfiguration.approveAppealPermission);
        SppPlayer sppPlayer = playerManager.getOnlinePlayer(player.getUniqueId())
            .orElseThrow((() -> new PlayerNotFoundException(player.getName())));
        if (banAppealConfiguration.resolveReasonEnabled) {
            messages.send(player, "&1===================================================", messages.prefixBans);
            messages.send(player, "&6       You have chosen to approve this appeal", messages.prefixBans);
            messages.send(player, "&6Type your closing reason in chat to approve the appeal", messages.prefixBans);
            messages.send(player, "&6      Type \"cancel\" to cancel approving the appeal ", messages.prefixBans);
            messages.send(player, "&1===================================================", messages.prefixBans);
            OnlinePlayerSession playerSession = sessionManager.get(player);
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messages.send(player, "&CYou have cancelled approving this appeal", messages.prefixBans);
                    return;
                }
                bukkitUtils.runTaskAsync(player, () -> appealService.approveAppeal(sppPlayer, appealId, message, AppealableType.BAN));
            });
        } else {
            bukkitUtils.runTaskAsync(player, () -> appealService.approveAppeal(sppPlayer, appealId, AppealableType.BAN));
        }
    }

    @GuiAction("manage-ban-appeals/reject")
    public void rejectAppeal(Player player, @GuiParam("appealId") int appealId) {
        permissionHandler.validate(player, banAppealConfiguration.rejectAppealPermission);
        SppPlayer sppPlayer = playerManager.getOnlinePlayer(player.getUniqueId())
            .orElseThrow((() -> new PlayerNotFoundException(player.getName())));
        if (banAppealConfiguration.resolveReasonEnabled) {
            messages.send(player, "&1==================================================", messages.prefixBans);
            messages.send(player, "&6        You have chosen to reject this appeal", messages.prefixBans);
            messages.send(player, "&6Type your closing reason in chat to reject the appeal", messages.prefixBans);
            messages.send(player, "&6        Type \"cancel\" to cancel closing the appeal ", messages.prefixBans);
            messages.send(player, "&1==================================================", messages.prefixBans);
            OnlinePlayerSession playerSession = sessionManager.get(player);
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messages.send(player, "&CYou have cancelled rejecting this appeal", messages.prefixBans);
                    return;
                }
                bukkitUtils.runTaskAsync(player, () -> appealService.rejectAppeal(sppPlayer, appealId, message, AppealableType.BAN));
            });
        } else {
            bukkitUtils.runTaskAsync(player, () -> appealService.rejectAppeal(sppPlayer, appealId, AppealableType.BAN));
        }
    }
}
