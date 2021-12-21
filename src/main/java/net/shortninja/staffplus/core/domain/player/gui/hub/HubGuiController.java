package net.shortninja.staffplus.core.domain.player.gui.hub;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.AsyncGui;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.templates.GuiTemplate;

import java.util.HashMap;

import static be.garagepoort.mcioc.gui.AsyncGui.async;
import static be.garagepoort.mcioc.gui.templates.GuiTemplate.template;

@IocBean
@GuiController
public class HubGuiController {

    @GuiAction("hub/view")
    public AsyncGui<GuiTemplate> getHubView() {
        return async(() -> template("gui/hub/hub.ftl", new HashMap<>()));
    }

}
