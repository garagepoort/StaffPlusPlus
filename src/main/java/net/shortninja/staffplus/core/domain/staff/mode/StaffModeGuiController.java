package net.shortninja.staffplus.core.domain.staff.mode;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mode.handler.CpsHandler;
import net.shortninja.staffplus.core.domain.staff.mode.handler.GadgetHandler;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

@IocBean
@GuiController
public class StaffModeGuiController {

    private final CpsHandler cpsHandler;
    private final GadgetHandler gadgetHandler;
    private final PlayerManager playerManager;

    public StaffModeGuiController(CpsHandler cpsHandler, GadgetHandler gadgetHandler, PlayerManager playerManager) {
        this.cpsHandler = cpsHandler;
        this.gadgetHandler = gadgetHandler;
        this.playerManager = playerManager;
    }

    @GuiAction("staff-mode/cps")
    public void startCps(Player player, @GuiParam("targetPlayerName") String targetPlayerName) {
        SppPlayer sppPlayer = playerManager.getOnlinePlayer(targetPlayerName).orElseThrow(() -> new PlayerNotFoundException(targetPlayerName));
        cpsHandler.startTest(player, sppPlayer.getPlayer());
    }

    @GuiAction("staff-mode/follow")
    public void follow(Player player, @GuiParam("targetPlayerName") String targetPlayerName) {
        SppPlayer sppPlayer = playerManager.getOnlinePlayer(targetPlayerName).orElseThrow(() -> new PlayerNotFoundException(targetPlayerName));
        gadgetHandler.onFollow(player, sppPlayer.getPlayer());
    }

}
