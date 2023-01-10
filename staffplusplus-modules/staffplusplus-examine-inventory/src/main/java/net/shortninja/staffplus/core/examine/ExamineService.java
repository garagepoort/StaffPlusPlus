package net.shortninja.staffplus.core.examine;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.tubinggui.GuiActionBuilder;
import be.garagepoort.mcioc.tubinggui.GuiActionService;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

@IocBean
public class ExamineService {

    private final GuiActionService guiActionService;

    public ExamineService(GuiActionService guiActionService) {
        this.guiActionService = guiActionService;
    }

    public void examine(Player player, SppPlayer targetPlayer) {
        if (targetPlayer == null) {
            return;
        }

        guiActionService.executeAction(player, GuiActionBuilder.builder()
            .action("examine/view")
            .param("targetPlayerName", targetPlayer.getUsername())
            .build());
    }
}
