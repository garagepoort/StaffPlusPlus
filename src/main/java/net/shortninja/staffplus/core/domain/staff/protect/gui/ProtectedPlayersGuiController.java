package net.shortninja.staffplus.core.domain.staff.protect.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.AsyncGui;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.templates.GuiTemplate;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.IPlayerSession;
import net.shortninja.staffplusplus.session.SppPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static be.garagepoort.mcioc.gui.AsyncGui.async;

@IocBean
@GuiController
public class ProtectedPlayersGuiController {

    private final PlayerManager playerManager;
    private final OnlineSessionsManager onlineSessionsManager;

    public ProtectedPlayersGuiController(PlayerManager playerManager, OnlineSessionsManager onlineSessionsManager) {
        this.playerManager = playerManager;
        this.onlineSessionsManager = onlineSessionsManager;
    }


    @GuiAction("protected-players/view")
    public AsyncGui<GuiTemplate> getOverview(@GuiParam(value = "page", defaultValue = "0") int page) {
        return async(() -> {
            List<SppPlayer> players = onlineSessionsManager.getAll().stream()
                .filter(IPlayerSession::isProtected)
                .map(s -> playerManager.getOnlinePlayer(s.getUuid()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
            List<SppPlayer> pagedPlayers = JavaUtils.getPageOfList(players, page, 45);

            HashMap<String, Object> params = new HashMap<>();
            params.put("players", pagedPlayers);
            return GuiTemplate.template("gui/protect/player-overview.ftl", params);
        });
    }

}