package net.shortninja.staffplus.core.domain.staff.ban.playerbans.gui;

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
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.Ban;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanService;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.database.BansRepository;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static be.garagepoort.mcioc.gui.AsyncGui.async;
import static be.garagepoort.mcioc.gui.templates.GuiTemplate.template;

@IocBean
@GuiController
public class BanGuiController {

    private static final String CANCEL = "cancel";
    private static final int PAGE_SIZE = 45;

    private final Messages messages;
    private final BanService banService;
    private final BansRepository bansRepository;
    private final OnlineSessionsManager sessionManager;
    private final PlayerManager playerManager;

    public BanGuiController(Messages messages,
                            BanService banService,
                            BansRepository bansRepository, OnlineSessionsManager sessionManager,
                            PlayerManager playerManager) {
        this.messages = messages;
        this.banService = banService;
        this.bansRepository = bansRepository;
        this.sessionManager = sessionManager;
        this.playerManager = playerManager;
    }

    @GuiAction("manage-bans/view/overview")
    public AsyncGui<GuiTemplate> getBannedPlayersOverview(
        @GuiParam("targetPlayerName") String targetPlayerName,
        @GuiParam(value = "page", defaultValue = "0") int page) {

        SppPlayer target = null;
        if (StringUtils.isNotBlank(targetPlayerName)) {
            target = playerManager.getOnOrOfflinePlayer(targetPlayerName).orElseThrow((() -> new PlayerNotFoundException(targetPlayerName)));
        }
        SppPlayer finalTarget = target;
        return async(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("bans", getBans(finalTarget, page));
            return template("gui/bans/bans-overview.ftl", params);
        });
    }

    private List<Ban> getBans(SppPlayer target, int page) {
        if (target == null) {
            return bansRepository.getActiveBans(page * PAGE_SIZE, PAGE_SIZE);
        }
        return bansRepository.getBansForPlayerPaged(target.getId(), page * PAGE_SIZE, PAGE_SIZE);
    }

    @GuiAction("manage-bans/view/detail")
    public AsyncGui<GuiTemplate> getBanDetailView(@GuiParam("banId") int banId) {
        return async(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("ban", banService.getById(banId));
            return template("gui/bans/ban-detail.ftl", params);
        });
    }

    @GuiAction("manage-bans/view/history")
    public AsyncGui<GuiTemplate> getBansPlayersHistory(@GuiParam(value = "page", defaultValue = "0") int page,
                                                       @GuiParam("targetPlayerName") String targetPlayerName) {
        return async(() -> {
            SppPlayer target = playerManager.getOnOrOfflinePlayer(targetPlayerName).orElseThrow(() -> new PlayerNotFoundException(targetPlayerName));
            Map<String, Object> params = new HashMap<>();
            params.put("bans", bansRepository.getBansForPlayerPaged(target.getId(), page * PAGE_SIZE, PAGE_SIZE));
            params.put("target", target);
            return template("gui/bans/bans-history-overview.ftl", params);
        });
    }


    @GuiAction("manage-bans/unban")
    public void unban(Player player, @GuiParam("banId") int banId) {
        messages.send(player, "&1=====================================================", messages.prefixGeneral);
        messages.send(player, "&6         You have chosen to unban this player", messages.prefixGeneral);
        messages.send(player, "&6Type your reason for unbanning this player in chat", messages.prefixGeneral);
        messages.send(player, "&6        Type \"cancel\" to cancel the unban ", messages.prefixGeneral);
        messages.send(player, "&1=====================================================", messages.prefixGeneral);

        OnlinePlayerSession playerSession = sessionManager.get(player);
        playerSession.setChatAction((player1, message) -> {
            if (message.equalsIgnoreCase(CANCEL)) {
                messages.send(player, "&CYou have cancelled unbanning this player", messages.prefixReports);
                return;
            }
            banService.unban(player, banId, message);
        });
    }

}