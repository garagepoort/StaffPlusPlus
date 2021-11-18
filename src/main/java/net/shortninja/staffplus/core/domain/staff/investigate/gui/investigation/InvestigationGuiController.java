package net.shortninja.staffplus.core.domain.staff.investigate.gui.investigation;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.AsyncGui;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.templates.GuiTemplate;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static be.garagepoort.mcioc.gui.AsyncGui.async;
import static be.garagepoort.mcioc.gui.templates.GuiTemplate.template;

@IocBean
@GuiController
public class InvestigationGuiController {

    private static final int PAGE_SIZE = 45;
    private final PlayerManager playerManager;
    private final InvestigationService investigationService;
    private final BukkitUtils bukkitUtils;

    public InvestigationGuiController(PlayerManager playerManager, InvestigationService investigationService, BukkitUtils bukkitUtils) {
        this.playerManager = playerManager;
        this.investigationService = investigationService;
        this.bukkitUtils = bukkitUtils;
    }

    @GuiAction("manage-investigations/view/overview")
    public AsyncGui<GuiTemplate> getOverview(@GuiParam(value = "page", defaultValue = "0") int page,
                                             @GuiParam("targetPlayer") String targetPlayer) {
        return async(() -> {
            SppPlayer target = null;
            if (StringUtils.isNotBlank(targetPlayer)) {
                target = playerManager.getOnOrOfflinePlayer(targetPlayer).orElseThrow(() -> new PlayerNotFoundException("Player not found for name: [" + targetPlayer + "]"));
            }

            Map<String, Object> params = new HashMap<>();
            params.put("investigations", getInvestigations(target, page * PAGE_SIZE, PAGE_SIZE));

            return template("gui/investigations/investigations-overview.ftl", params);
        });
    }

    private List<Investigation> getInvestigations(SppPlayer target, int offset, int amount) {
        if (target == null) {
            return investigationService.getAllInvestigations(offset, amount);
        }
        return investigationService.getInvestigationsForInvestigated(target, offset, amount);
    }

    @GuiAction("manage-investigations/view/detail")
    public AsyncGui<GuiTemplate> getDetail(@GuiParam("investigationId") int investigationId) {
        return async(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("investigation", investigationService.getInvestigation(investigationId));

            return template("gui/investigations/investigation-detail.ftl", params);
        });
    }

    @GuiAction("manage-investigations/pause")
    public void pauseInvestigation(Player player) {
        bukkitUtils.runTaskAsync(player, () -> investigationService.pauseInvestigation(player));
    }

    @GuiAction("manage-investigations/resume")
    public void resumeInvestigation(Player player, @GuiParam("investigationId") int investigationId) {
        Investigation investigation = investigationService.getInvestigation(investigationId);

        if (investigation.getInvestigatedUuid().isPresent()) {
            SppPlayer investigated = playerManager.getOnOrOfflinePlayer(investigation.getInvestigatedUuid().get())
                .orElseThrow(() -> new BusinessException("Can't resume investigation. Player not found."));
            bukkitUtils.runTaskAsync(player, () -> investigationService.resumeInvestigation(player, investigated));
        } else {
            bukkitUtils.runTaskAsync(player, () -> investigationService.resumeInvestigation(player, investigation.getId()));
        }
    }

    @GuiAction("manage-investigations/conclude")
    public void concludeInvestigation(Player player,
                                      @GuiParam("investigationId") int investigationId) {
        bukkitUtils.runTaskAsync(player, () -> investigationService.concludeInvestigation(player, investigationId));
    }
}
