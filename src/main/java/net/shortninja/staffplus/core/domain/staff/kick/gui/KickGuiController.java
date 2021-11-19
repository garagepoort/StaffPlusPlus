package net.shortninja.staffplus.core.domain.staff.kick.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.templates.GuiTemplate;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.common.exceptions.PlayerOfflineException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.kick.KickService;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static be.garagepoort.mcioc.gui.templates.GuiTemplate.template;

@IocBean
@GuiController
public class KickGuiController {

    private final KickService kickService;
    private final PlayerManager playerManager;
    private final Options options;

    public KickGuiController(KickService kickService, PlayerManager playerManager, Options options) {
        this.kickService = kickService;
        this.playerManager = playerManager;
        this.options = options;
    }

    @GuiAction("manage-kicks/view/kick/reason-select")
    public GuiTemplate getKickReasonSelect(@GuiParam("targetPlayerName") String targetPlayerName) {
        SppPlayer target = playerManager.getOnOrOfflinePlayer(targetPlayerName).orElseThrow((() -> new PlayerNotFoundException(targetPlayerName)));
        Map<String, Object> params = new HashMap<>();
        params.put("target", target);
        params.put("reasons", options.kickConfiguration.getKickReasons());
        return template("gui/kicks/kick-reason-selection.ftl", params);
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
