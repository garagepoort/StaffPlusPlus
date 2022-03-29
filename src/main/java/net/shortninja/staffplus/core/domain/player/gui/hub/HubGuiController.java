package net.shortninja.staffplus.core.domain.player.gui.hub;

import be.garagepoort.mcioc.tubinggui.AsyncGui;
import be.garagepoort.mcioc.tubinggui.GuiAction;
import be.garagepoort.mcioc.tubinggui.GuiController;
import be.garagepoort.mcioc.tubinggui.GuiParam;
import be.garagepoort.mcioc.tubinggui.model.TubingGui;
import be.garagepoort.mcioc.tubinggui.templates.GuiTemplate;
import net.shortninja.staffplus.core.domain.player.gui.hub.views.ColorViewBuilder;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static be.garagepoort.mcioc.tubinggui.AsyncGui.async;
import static be.garagepoort.mcioc.tubinggui.templates.GuiTemplate.template;

@GuiController
public class HubGuiController {

    private final ColorViewBuilder colorViewBuilder;
    private final PlayerSettingsRepository playerSettingsRepository;

    public HubGuiController(ColorViewBuilder colorViewBuilder, PlayerSettingsRepository playerSettingsRepository) {
        this.colorViewBuilder = colorViewBuilder;
        this.playerSettingsRepository = playerSettingsRepository;
    }

    @GuiAction("hub/view")
    public AsyncGui<GuiTemplate> getHubView(Player player) {
        return async(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("settings", playerSettingsRepository.get(player));
            return template("gui/hub/hub.ftl", params);
        });
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
