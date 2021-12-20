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
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.Ban;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanService;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.Appeal;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.AppealService;
import net.shortninja.staffplusplus.appeals.AppealableType;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static be.garagepoort.mcioc.gui.AsyncGui.async;

@IocBean
@GuiController
public class BanAppealGuiController {

    private static final String CANCEL = "cancel";

    private final AppealService appealService;
    private final BanService banService;
    private final Messages messages;
    private final OnlineSessionsManager sessionManager;
    private final BukkitUtils bukkitUtils;
    private final BanAppealConfiguration banAppealConfiguration;

    public BanAppealGuiController(AppealService appealService,
                                  BanService banService,
                                  Messages messages,
                                  OnlineSessionsManager sessionManager,
                                  BukkitUtils bukkitUtils,
                                  BanAppealConfiguration banAppealConfiguration) {
        this.appealService = appealService;
        this.banService = banService;
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.bukkitUtils = bukkitUtils;
        this.banAppealConfiguration = banAppealConfiguration;
    }

    @GuiAction("manage-ban-appeals/view/detail")
    public AsyncGui<GuiTemplate> getAppealDetail(@GuiParam("appealId") int appealId) {
        return async(() -> {
            Appeal appeal = appealService.getAppeal(appealId);
            Ban ban = banService.getById(appealId);

            HashMap<String, Object> params = new HashMap<>();
            params.put("appeal", appeal);
            return GuiTemplate.template("gui/bans/appeal-detail.ftl", params);
        });
    }

    @GuiAction("manage-ban-appeals/view/create/reason-select")
    public GuiTemplate getCreateAppealReasonSelectView(@GuiParam("banId") int banId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("action", "manage-ban-appeals/create?banId=" + banId);
        params.put("reasons", banAppealConfiguration.appealReasons);
        return GuiTemplate.template("gui/appeals/appeal-reason-select.ftl", params);
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

            bukkitUtils.runTaskAsync(player, () -> appealService.addAppeal(player, ban, input));
        });
    }

    @GuiAction("manage-ban-appeals/create")
    public void createAppeal(Player player, @GuiParam("banId") int banId, @GuiParam("reason") String reason) {
        bukkitUtils.runTaskAsync(player, () -> {
            Ban ban = banService.getById(banId);
            appealService.addAppeal(player, ban, reason);
        });
    }

    @GuiAction("manage-ban-appeals/approve")
    public void approveAppeal(Player player, @GuiParam("appealId") int appealId) {
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
                bukkitUtils.runTaskAsync(player, () -> appealService.approveAppeal(player, appealId, message, AppealableType.BAN));
            });
        } else {
            bukkitUtils.runTaskAsync(player, () -> appealService.approveAppeal(player, appealId, AppealableType.BAN));
        }
    }

    @GuiAction("manage-ban-appeals/reject")
    public void rejectAppeal(Player player, @GuiParam("appealId") int appealId) {
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
                bukkitUtils.runTaskAsync(player, () -> appealService.rejectAppeal(player, appealId, message, AppealableType.BAN));
            });
        } else {
            bukkitUtils.runTaskAsync(player, () -> appealService.rejectAppeal(player, appealId, AppealableType.BAN));
        }
    }
}
