package net.shortninja.staffplus.core.domain.player.gui.hub;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.domain.player.gui.hub.views.HubViewBuilder;
import org.bukkit.entity.Player;

@IocBean
@GuiController
public class HubGuiController {

    private final HubViewBuilder hubViewBuilder;

    public HubGuiController(HubViewBuilder hubViewBuilder) {
        this.hubViewBuilder = hubViewBuilder;
    }

    @GuiAction("hub/view")
    public TubingGui getHubView(Player player) {
        return hubViewBuilder.buildGui(player);
    }

}
