package net.shortninja.staffplus.core.domain.staff.teleport.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.common.exceptions.PlayerOfflineException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.teleport.TeleportService;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

@IocBean
@GuiController
public class TeleportGuiController {

    private final PlayerManager playerManager;
    private final TeleportService teleportService;

    public TeleportGuiController(PlayerManager playerManager, TeleportService teleportService) {
        this.playerManager = playerManager;
        this.teleportService = teleportService;
    }

    @GuiAction("teleport")
    public void teleport(Player player, @GuiParam("targetPlayerName") String targetPlayerName) {
        SppPlayer sppPlayer = playerManager.getOnOrOfflinePlayer(targetPlayerName).orElseThrow(() -> new PlayerNotFoundException(targetPlayerName));
        if (!sppPlayer.isOnline()) {
            throw new PlayerOfflineException();
        }
        teleportService.teleportToPlayer(player, sppPlayer.getPlayer());
    }
}
