package net.shortninja.staffplus.core.domain.player.gui.hub;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.domain.player.gui.hub.views.ColorViewBuilder;
import net.shortninja.staffplus.core.domain.player.gui.hub.views.HubViewBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@IocBean
@GuiController
public class HubGuiController {

    private final HubViewBuilder hubViewBuilder;
    private final ColorViewBuilder colorViewBuilder;
    private final SessionManagerImpl sessionManager;

    public HubGuiController(HubViewBuilder hubViewBuilder, ColorViewBuilder colorViewBuilder, SessionManagerImpl sessionManager) {
        this.hubViewBuilder = hubViewBuilder;
        this.colorViewBuilder = colorViewBuilder;
        this.sessionManager = sessionManager;
    }

    @GuiAction("hub/view")
    public TubingGui getHubView(Player player) {
        return hubViewBuilder.buildGui(player);
    }

    @GuiAction("hub/view/color-select")
    public TubingGui getGlassView(Player player) {
        return colorViewBuilder.buildGui();
    }

    @GuiAction("hub/change-color")
    public String changeGlass(Player player, @GuiParam("color") String color) {
        Material material = Material.valueOf(color);
        sessionManager.get(player.getUniqueId()).setGlassColor(material);
        return "hub/view";
    }

}
