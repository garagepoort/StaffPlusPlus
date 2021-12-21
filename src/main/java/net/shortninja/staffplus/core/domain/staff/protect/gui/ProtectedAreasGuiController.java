package net.shortninja.staffplus.core.domain.staff.protect.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.AsyncGui;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.templates.GuiTemplate;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.staff.protect.ProtectService;
import net.shortninja.staffplus.core.domain.staff.protect.ProtectedArea;
import net.shortninja.staffplus.core.domain.staff.protect.config.ProtectConfiguration;
import net.shortninja.staffplus.core.domain.staff.teleport.TeleportService;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static be.garagepoort.mcioc.gui.AsyncGui.async;
import static be.garagepoort.mcioc.gui.templates.GuiTemplate.template;

@IocBean
@GuiController
public class ProtectedAreasGuiController {

    private static final int PAGE_SIZE = 45;

    private final ProtectService protectService;
    private final TeleportService teleportService;
    private final BukkitUtils bukkitUtils;
    private final ProtectConfiguration protectConfiguration;

    public ProtectedAreasGuiController(ProtectService protectService,
                                       TeleportService teleportService,
                                       BukkitUtils bukkitUtils,
                                       ProtectConfiguration protectConfiguration) {
        this.protectService = protectService;
        this.teleportService = teleportService;
        this.bukkitUtils = bukkitUtils;
        this.protectConfiguration = protectConfiguration;
    }


    @GuiAction("protected-areas/view")
    public AsyncGui<GuiTemplate> getOverview(@GuiParam(value = "page", defaultValue = "0") int page) {
        return async(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("title", protectConfiguration.modeGuiProtectedAreasTitle);
            params.put("areas", protectService.getAllProtectedAreasPaginated(page * PAGE_SIZE, PAGE_SIZE));
            return template("gui/protect/area-overview.ftl", params);
        });
    }

    @GuiAction("protected-areas/view/detail")
    public AsyncGui<GuiTemplate> getAreaDetail(@GuiParam("areaId") int areaId) {
        return async(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("area", protectService.getById(areaId));
            return template("gui/protect/area-detail.ftl", params);
        });
    }

    @GuiAction("protected-areas/delete")
    public void delete(Player player, @GuiParam("areaId") int areaId) {
        bukkitUtils.runTaskAsync(player, () -> protectService.deleteProtectedArea(player, areaId));
    }

    @GuiAction("protected-areas/teleport")
    public void teleport(Player player, @GuiParam("areaId") int areaId) {
        ProtectedArea protectedArea = protectService.getById(areaId);
        teleportService.teleportSelf(player, protectedArea.getCornerPoint1());
    }

}