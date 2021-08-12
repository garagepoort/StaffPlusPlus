package net.shortninja.staffplus.core.domain.player.gui.hub;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.AsyncGui;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.domain.player.gui.hub.views.ColorViewBuilder;
import net.shortninja.staffplus.core.domain.player.gui.hub.views.HubViewBuilder;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static be.garagepoort.mcioc.gui.AsyncGui.async;

@IocBean
@GuiController
public class HubGuiController {

    private final HubViewBuilder hubViewBuilder;
    private final ColorViewBuilder colorViewBuilder;
    private final PlayerSettingsRepository playerSettingsRepository;

    public HubGuiController(HubViewBuilder hubViewBuilder, ColorViewBuilder colorViewBuilder, PlayerSettingsRepository playerSettingsRepository) {
        this.hubViewBuilder = hubViewBuilder;
        this.colorViewBuilder = colorViewBuilder;
        this.playerSettingsRepository = playerSettingsRepository;
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
    public AsyncGui<String> changeGlass(Player player, @GuiParam("color") String color) {
        return async(() -> {
            Material material = Material.valueOf(color);
            PlayerSettings session = playerSettingsRepository.get(player);
            session.setGlassColor(material);
            playerSettingsRepository.save(session);
            return "hub/view";
        });
    }

}
