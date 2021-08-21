package net.shortninja.staffplus.core.domain.staff.freeze.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiActionReturnType;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeRequest;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

@IocBean
@GuiController
public class FreezeGuiController {

    private final PlayerManager playerManager;
    private final FreezeHandler freezeHandler;
    private final OnlineSessionsManager onlineSessionsManager;

    public FreezeGuiController(PlayerManager playerManager, FreezeHandler freezeHandler, OnlineSessionsManager onlineSessionsManager) {
        this.playerManager = playerManager;
        this.freezeHandler = freezeHandler;
        this.onlineSessionsManager = onlineSessionsManager;
    }

    @GuiAction("manage-frozen/freeze")
    public GuiActionReturnType freezePlayer(Player player, @GuiParam("targetPlayerName") String targetPlayerName, @GuiParam("enable") Boolean enable) {
        SppPlayer sppPlayer = playerManager.getOnlinePlayer(targetPlayerName).orElseThrow(() -> new PlayerNotFoundException(targetPlayerName));
        OnlinePlayerSession session = onlineSessionsManager.get(sppPlayer.getPlayer());
        freezeHandler.execute(new FreezeRequest(player, sppPlayer.getPlayer(), enable == null ? !session.isFrozen() : enable));
        return GuiActionReturnType.KEEP_OPEN;
    }
}
