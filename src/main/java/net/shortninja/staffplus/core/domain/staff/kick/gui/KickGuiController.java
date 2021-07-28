package net.shortninja.staffplus.core.domain.staff.kick.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.common.exceptions.PlayerOfflineException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.kick.KickService;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

@IocBean
@GuiController
public class KickGuiController {

    private final KickService kickService;
    private final KickReasonSelectViewBuilder kickReasonSelectViewBuilder;
    private final PlayerManager playerManager;

    public KickGuiController(KickService kickService, KickReasonSelectViewBuilder kickReasonSelectViewBuilder, PlayerManager playerManager) {
        this.kickService = kickService;
        this.kickReasonSelectViewBuilder = kickReasonSelectViewBuilder;
        this.playerManager = playerManager;
    }

    @GuiAction("manage-kicks/view/kick/reason-select")
    public TubingGui getKickReasonSelect(@GuiParam("targetPlayerName") String targetPlayerName) {
        return kickReasonSelectViewBuilder.buildGui(targetPlayerName);
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
