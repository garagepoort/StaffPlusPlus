package net.shortninja.staffplus.core.domain.staff.kick.gui;

import be.garagepoort.mcioc.tubinggui.AsyncGui;
import be.garagepoort.mcioc.tubinggui.GuiAction;
import be.garagepoort.mcioc.tubinggui.GuiController;
import be.garagepoort.mcioc.tubinggui.GuiParam;
import be.garagepoort.mcioc.tubinggui.templates.GuiTemplate;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.common.exceptions.PlayerOfflineException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.kick.KickService;
import net.shortninja.staffplus.core.domain.staff.kick.config.KickConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static be.garagepoort.mcioc.tubinggui.AsyncGui.async;
import static be.garagepoort.mcioc.tubinggui.templates.GuiTemplate.template;

@GuiController
public class KickGuiController {

    private static final int PAGE_SIZE = 45;
    private final KickService kickService;
    private final PlayerManager playerManager;
    private final KickConfiguration kickConfiguration;

    public KickGuiController(KickService kickService, PlayerManager playerManager, KickConfiguration kickConfiguration) {
        this.kickService = kickService;
        this.playerManager = playerManager;
        this.kickConfiguration = kickConfiguration;
    }

    @GuiAction("manage-kicks/view/kick/reason-select")
    public GuiTemplate getKickReasonSelect(@GuiParam("targetPlayerName") String targetPlayerName) {
        SppPlayer target = playerManager.getOnOrOfflinePlayer(targetPlayerName).orElseThrow((() -> new PlayerNotFoundException(targetPlayerName)));
        Map<String, Object> params = new HashMap<>();
        params.put("target", target);
        params.put("reasons", kickConfiguration.kickReasons);
        return template("gui/kicks/kick-reason-selection.ftl", params);
    }

    @GuiAction("manage-kicks/view/overview")
    public AsyncGui<GuiTemplate> getKickOverview(@GuiParam("targetPlayerName") String targetPlayerName, @GuiParam(value = "page", defaultValue = "0") int page) {
        return async(() -> {
            SppPlayer target = playerManager.getOnOrOfflinePlayer(targetPlayerName).orElseThrow((() -> new PlayerNotFoundException(targetPlayerName)));
            Map<String, Object> params = new HashMap<>();
            params.put("kicks", kickService.getAllKicksForPlayer(target.getId(), page * PAGE_SIZE, PAGE_SIZE));
            return template("gui/kicks/kick-overview.ftl", params);
        });
    }

    @GuiAction("manage-kicks/kick")
    public void kick(Player player,
                     @GuiParam("targetPlayerName") String targetPlayerName,
                     @GuiParam("reason") String reason) {
        SppPlayer target = playerManager.getOnOrOfflinePlayer(targetPlayerName).orElseThrow(() -> new PlayerNotFoundException(targetPlayerName));
        if (!target.isOnline()) {
            throw new PlayerOfflineException();
        }
        kickService.kick(player, target, reason);
    }
}
