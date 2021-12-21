package net.shortninja.staffplus.core.domain.player.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.templates.GuiTemplate;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static be.garagepoort.mcioc.gui.templates.GuiTemplate.template;

@IocBean
@GuiController
public class StaffMembersGuiController {

    @ConfigProperty("permissions:member")
    private String permissionMember;

    private final Options options;
    private final OnlineSessionsManager sessionManager;
    private final PermissionHandler permissionHandler;
    private final PlayerManager playerManager;

    public StaffMembersGuiController(Options options,
                                     OnlineSessionsManager sessionManager,
                                     PermissionHandler permissionHandler,
                                     PlayerManager playerManager) {
        this.options = options;
        this.sessionManager = sessionManager;
        this.permissionHandler = permissionHandler;
        this.playerManager = playerManager;
    }

    @GuiAction("membersGUI")
    public GuiTemplate getItems(@GuiParam(value = "page", defaultValue = "0") int page) {
        List<SppPlayer> players = options.staffItemsConfiguration.getCounterModeConfiguration().isModeCounterShowStaffMode() ? getModePlayers() : playerManager.getOnlineSppPlayers();
        List<SppPlayer> sppPlayers = players.stream().filter(p -> permissionHandler.has(p.getPlayer(), permissionMember)).collect(Collectors.toList());
        List<SppPlayer> pageOfList = JavaUtils.getPageOfList(sppPlayers, page, 45);

        HashMap<String, Object> params = new HashMap<>();
        params.put("title", options.staffItemsConfiguration.getCounterModeConfiguration().getTitle());
        params.put("players", pageOfList);
        return template("gui/player/player-overview.ftl", params);
    }

    private List<SppPlayer> getModePlayers() {
        return sessionManager.getAll().stream()
            .map(s -> playerManager.getOnlinePlayer(s.getUuid()))
            .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
            .collect(Collectors.toList());
    }

}
