package net.shortninja.staffplus.core.domain.staff.freeze.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiActionReturnType;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeRequest;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

@IocBean
@GuiController
public class FreezeGuiController {

    private final PlayerManager playerManager;
    private final FreezeHandler freezeHandler;

    public FreezeGuiController(PlayerManager playerManager, FreezeHandler freezeHandler) {
        this.playerManager = playerManager;
        this.freezeHandler = freezeHandler;
    }

    @GuiAction("manage-frozen/freeze")
    public GuiActionReturnType freezePlayer(Player player, @GuiParam("targetPlayerUuid") String targetPlayerName, @GuiParam("enable") Boolean enable) {
        SppPlayer sppPlayer = playerManager.getOnlinePlayer(UUID.fromString(targetPlayerName)).orElseThrow(() -> new PlayerNotFoundException(targetPlayerName));
        freezeHandler.execute(new FreezeRequest(player, sppPlayer.getPlayer(), enable == null ? !freezeHandler.isFrozen(sppPlayer.getId()) : enable));
        return GuiActionReturnType.KEEP_OPEN;
    }
}
