package net.shortninja.staffplus.core.domain.staff.protect.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.domain.staff.protect.ProtectService;
import net.shortninja.staffplus.core.domain.staff.protect.ProtectedArea;
import net.shortninja.staffplus.core.domain.staff.protect.gui.views.ManageProtectedAreaViewBuilder;
import net.shortninja.staffplus.core.domain.staff.protect.gui.views.ProtectedAreasViewBuilder;
import net.shortninja.staffplus.core.domain.staff.teleport.TeleportService;
import org.bukkit.entity.Player;

@IocBean
@GuiController
public class ProtectedAreasGuiController {

    private final ProtectedAreasViewBuilder protectedAreasViewBuilder;
    private final ProtectService protectService;
    private final ManageProtectedAreaViewBuilder manageProtectedAreaViewBuilder;
    private final TeleportService teleportService;

    public ProtectedAreasGuiController(ProtectedAreasViewBuilder protectedAreasViewBuilder, ProtectService protectService, ManageProtectedAreaViewBuilder manageProtectedAreaViewBuilder, TeleportService teleportService) {
        this.protectedAreasViewBuilder = protectedAreasViewBuilder;
        this.protectService = protectService;
        this.manageProtectedAreaViewBuilder = manageProtectedAreaViewBuilder;
        this.teleportService = teleportService;
    }


    @GuiAction("protected-areas/view")
    public TubingGui getOverview(Player player, @GuiParam("page") int page, @GuiParam("backAction") String backAction) {
        return protectedAreasViewBuilder.buildGui(page, backAction);
    }

    @GuiAction("protected-areas/view/detail")
    public TubingGui getAreaDetail(Player player, @GuiParam("areaId") int areaId, @GuiParam("backAction") String backAction) {
        ProtectedArea protectedArea = protectService.getById(areaId);
        return manageProtectedAreaViewBuilder.buildGui(protectedArea, backAction);
    }

    @GuiAction("protected-areas/delete")
    public void delete(Player player, @GuiParam("areaId") int areaId) {
        protectService.deleteProtectedArea(player, areaId);
    }

    @GuiAction("protected-areas/teleport")
    public void teleport(Player player, @GuiParam("areaId") int areaId) {
        ProtectedArea protectedArea = protectService.getById(areaId);
        teleportService.teleportSelf(player, protectedArea.getCornerPoint1());
    }

}