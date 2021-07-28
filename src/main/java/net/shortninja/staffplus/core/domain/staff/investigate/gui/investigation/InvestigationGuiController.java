package net.shortninja.staffplus.core.domain.staff.investigate.gui.investigation;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.CurrentAction;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.views.InvestigationDetailViewBuilder;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.views.InvestigationsOverviewViewBuilder;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

@IocBean
@GuiController
public class InvestigationGuiController {

    private final InvestigationsOverviewViewBuilder investigationsOverviewViewBuilder;
    private final InvestigationDetailViewBuilder investigationViewBuilder;
    private final PlayerManager playerManager;
    private final InvestigationService investigationService;

    public InvestigationGuiController(InvestigationsOverviewViewBuilder investigationsOverviewViewBuilder,
                                      InvestigationDetailViewBuilder investigationViewBuilder,
                                      PlayerManager playerManager, InvestigationService investigationService) {
        this.investigationsOverviewViewBuilder = investigationsOverviewViewBuilder;
        this.investigationViewBuilder = investigationViewBuilder;
        this.playerManager = playerManager;
        this.investigationService = investigationService;
    }

    @GuiAction("manage-investigations/view/overview")
    public TubingGui getOverview(@GuiParam(value = "page", defaultValue = "0") int page,
                                 @GuiParam("targetPlayer") String targetPlayer,
                                 @GuiParam("backAction") String backAction,
                                 @CurrentAction String currentAction) {
        if (StringUtils.isNotBlank(targetPlayer)) {
            SppPlayer sppPlayer = playerManager.getOnOrOfflinePlayer(targetPlayer).orElseThrow(() -> new PlayerNotFoundException("Player not found for name: [" + targetPlayer + "]"));
            return investigationsOverviewViewBuilder.buildGui(sppPlayer, page, backAction, currentAction);
        }
        return investigationsOverviewViewBuilder.buildGui(null, page, backAction, currentAction);
    }

    @GuiAction("manage-investigations/view/detail")
    public TubingGui getDetail(@GuiParam("investigationId") int investigationId,
                               @GuiParam("backAction") String backAction,
                               @CurrentAction String currentAction) {
        Investigation investigation = investigationService.getInvestigation(investigationId);
        return investigationViewBuilder.buildGui(investigation, currentAction, backAction);
    }

    @GuiAction("manage-investigations/pause")
    public void pauseInvestigation(Player player) {
        investigationService.pauseInvestigation(player);
    }

    @GuiAction("manage-investigations/resume")
    public void resumeInvestigation(Player player, @GuiParam("investigationId") int investigationId) {
        Investigation investigation = investigationService.getInvestigation(investigationId);

        if (investigation.getInvestigatedUuid().isPresent()) {
            SppPlayer investigated = playerManager.getOnOrOfflinePlayer(investigation.getInvestigatedUuid().get())
                .orElseThrow(() -> new BusinessException("Can't resume investigation. Player not found."));
            investigationService.resumeInvestigation(player, investigated);
        } else {
            investigationService.resumeInvestigation(player, investigation.getId());
        }
    }

    @GuiAction("manage-investigations/conclude")
    public void concludeInvestigation(Player player,
                                      @GuiParam("investigationId") int investigationId) {
        investigationService.concludeInvestigation(player, investigationId);
    }
}
