package net.shortninja.staffplus.core.domain.player.gui.hub;

import be.garagepoort.mcioc.tubinggui.AsyncGui;
import be.garagepoort.mcioc.tubinggui.GuiAction;
import be.garagepoort.mcioc.tubinggui.GuiController;
import be.garagepoort.mcioc.tubinggui.templates.GuiTemplate;

import java.util.HashMap;

import static be.garagepoort.mcioc.tubinggui.AsyncGui.async;
import static be.garagepoort.mcioc.tubinggui.templates.GuiTemplate.template;

@GuiController
public class HubGuiController {

    @GuiAction("hub/view")
    public AsyncGui<GuiTemplate> getHubView() {
        return async(() -> template("gui/hub/hub.ftl", new HashMap<>()));
    }

}
