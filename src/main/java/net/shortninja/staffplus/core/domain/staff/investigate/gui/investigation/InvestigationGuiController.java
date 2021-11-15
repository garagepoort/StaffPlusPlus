package net.shortninja.staffplus.core.domain.staff.investigate.gui.investigation;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.AsyncGui;
import be.garagepoort.mcioc.gui.CurrentAction;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.model.TubingGui;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.views.InvestigationDetailViewBuilder;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.views.InvestigationsOverviewViewBuilder;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import static be.garagepoort.mcioc.gui.AsyncGui.async;

@IocBean
@GuiController
public class InvestigationGuiController {

    private final InvestigationsOverviewViewBuilder investigationsOverviewViewBuilder;
    private final InvestigationDetailViewBuilder investigationViewBuilder;
    private final PlayerManager playerManager;
    private final InvestigationService investigationService;
    private final BukkitUtils bukkitUtils;

    public InvestigationGuiController(InvestigationsOverviewViewBuilder investigationsOverviewViewBuilder,
                                      InvestigationDetailViewBuilder investigationViewBuilder,
                                      PlayerManager playerManager, InvestigationService investigationService, BukkitUtils bukkitUtils) {
        this.investigationsOverviewViewBuilder = investigationsOverviewViewBuilder;
        this.investigationViewBuilder = investigationViewBuilder;
        this.playerManager = playerManager;
        this.investigationService = investigationService;
        this.bukkitUtils = bukkitUtils;
    }

    @GuiAction("manage-investigations/view/overview")
    public AsyncGui<TubingGui> getOverview(@GuiParam(value = "page", defaultValue = "0") int page,
                                           @GuiParam("targetPlayer") String targetPlayer,
                                           @GuiParam("backAction") String backAction,
                                           @CurrentAction String currentAction) {
        if (StringUtils.isNotBlank(targetPlayer)) {
            SppPlayer sppPlayer = playerManager.getOnOrOfflinePlayer(targetPlayer).orElseThrow(() -> new PlayerNotFoundException("Player not found for name: [" + targetPlayer + "]"));
            return async(() -> investigationsOverviewViewBuilder.buildGui(sppPlayer, page, backAction, currentAction));
        }
        return async(() -> investigationsOverviewViewBuilder.buildGui(null, page, backAction, currentAction));
    }

    @GuiAction("manage-investigations/view/detail")
    public AsyncGui<TubingGui> getDetail(@GuiParam("investigationId") int investigationId,
                                         @GuiParam("backAction") String backAction,
                                         @CurrentAction String currentAction) {
        return async(() -> {
            Investigation investigation = investigationService.getInvestigation(investigationId);
            return investigationViewBuilder.buildGui(investigation, currentAction, backAction);
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
