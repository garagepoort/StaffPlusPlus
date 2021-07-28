package net.shortninja.staffplus.core.domain.staff.chests;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiActionReturnType;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

@IocBean
@GuiController
public class EnderchestGuiController {

    private final EnderChestService enderChestService;
    private final PlayerManager playerManager;

    public EnderchestGuiController(EnderChestService enderChestService, PlayerManager playerManager) {
        this.enderChestService = enderChestService;
        this.playerManager = playerManager;
    }

    @GuiAction("manage-enderchest/open")
    public GuiActionReturnType openEnderchest(Player player, @GuiParam("targetPlayerName") String targetPlayerName) {
        SppPlayer target = playerManager.getOnOrOfflinePlayer(targetPlayerName).orElseThrow(() -> new PlayerNotFoundException(targetPlayerName));
        enderChestService.openEnderChest(player, target);
        return GuiActionReturnType.KEEP_OPEN;
    }
}
