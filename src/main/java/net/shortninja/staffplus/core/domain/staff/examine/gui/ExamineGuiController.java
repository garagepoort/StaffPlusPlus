package net.shortninja.staffplus.core.domain.staff.examine.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

@IocBean
@GuiController
public class ExamineGuiController {

    private final PlayerManager playerManager;
    private final ExamineGuiViewBuilder examineGuiViewBuilder;

    public ExamineGuiController(PlayerManager playerManager, ExamineGuiViewBuilder examineGuiViewBuilder) {
        this.playerManager = playerManager;
        this.examineGuiViewBuilder = examineGuiViewBuilder;
    }

    @GuiAction("examine/view")
    public TubingGui examinePlayer(Player player, @GuiParam("targetPlayerName") String targetPlayerName) {
        SppPlayer sppPlayer = playerManager.getOnOrOfflinePlayer(targetPlayerName).orElseThrow(() -> new PlayerNotFoundException(targetPlayerName));
        return examineGuiViewBuilder.buildGui(player, sppPlayer);
    }
}
