package net.shortninja.staffplus.core.domain.staff.investigate.gui.evidence;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.AsyncGui;
import be.garagepoort.mcioc.gui.CurrentAction;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.model.TubingGui;
import be.garagepoort.mcioc.gui.templates.GuiTemplate;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationEvidenceService;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.views.EvidenceOverviewViewBuilder;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.views.InvestigationLinkEvidenceSelectionViewBuilder;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static be.garagepoort.mcioc.gui.AsyncGui.async;
import static be.garagepoort.mcioc.gui.templates.GuiTemplate.template;

@IocBean
@GuiController
public class InvestigationEvidenceGuiController {

    private final EvidenceOverviewViewBuilder evidenceOverviewViewBuilder;
    private final InvestigationService investigationService;
    private final InvestigationEvidenceService investigationEvidenceService;
    private final InvestigationLinkEvidenceSelectionViewBuilder investigationLinkEvidenceSelectionViewBuilder;
    private final BukkitUtils bukkitUtils;

    public InvestigationEvidenceGuiController(EvidenceOverviewViewBuilder evidenceOverviewViewBuilder,
                                              InvestigationService investigationService,
                                              InvestigationEvidenceService investigationEvidenceService,
                                              InvestigationLinkEvidenceSelectionViewBuilder investigationLinkEvidenceSelectionViewBuilder,
                                              BukkitUtils bukkitUtils) {
        this.evidenceOverviewViewBuilder = evidenceOverviewViewBuilder;
        this.investigationService = investigationService;
        this.investigationEvidenceService = investigationEvidenceService;
        this.investigationLinkEvidenceSelectionViewBuilder = investigationLinkEvidenceSelectionViewBuilder;
        this.bukkitUtils = bukkitUtils;
    }

    @GuiAction("manage-investigation-evidence/view")
    public AsyncGui<TubingGui> getEvidenceOverview(Player player,
                                                   @GuiParam(value = "page", defaultValue = "0") int page,
                                                   @GuiParam("investigationId") int investigationId,
                                                   @CurrentAction String currentAction,
                                                   @GuiParam("backAction") String backAction) {
        return async(() -> {
            Investigation investigation = investigationService.getInvestigation(investigationId);
            return evidenceOverviewViewBuilder.buildGui(player, investigation, page, currentAction, backAction);
        });
    }

    @GuiAction("manage-investigation-evidence/view/unlink")
    public GuiTemplate goToUnlinkEvidenceView(@GuiParam("evidenceId") int evidenceId,
                                              @GuiParam("investigationId") int investigationId,
                                              @GuiParam("backAction") String backAction) {
        String confirmAction = GuiActionBuilder.builder()
            .action("manage-investigation-evidence/unlink")
            .param("evidenceId", String.valueOf(evidenceId))
            .param("investigationId", String.valueOf(investigationId))
            .param("backAction", backAction)
            .build();

        HashMap<String, Object> params = new HashMap<>();
        params.put("confirmationMessage", "Are you sure you want to unlink evidence: (ID=" + evidenceId + ")");
        params.put("title", "Unlink evidence?");
        params.put("confirmAction", confirmAction);
        params.put("cancelAction", backAction);
        return template("gui/commons/confirmation.ftl", params);
    }

    @GuiAction("manage-investigation-evidence/unlink")
    public AsyncGui<String> unlinkEvidence(Player player,
                                           @GuiParam("evidenceId") int evidenceId,
                                           @GuiParam("investigationId") int investigationId,
                                           @GuiParam("backAction") String backAction) {
        return async(() -> {
            Investigation investigation = investigationService.getInvestigation(investigationId);
            investigationEvidenceService.unlinkEvidence(player, investigation, evidenceId);
            return backAction;
        });
    }

    @GuiAction("manage-investigation-evidence/view/investigation-link")
    public AsyncGui<TubingGui> getOverview(@GuiParam(value = "page", defaultValue = "0") int page,
                                           @CurrentAction String currentAction,
                                           @GuiParam("backAction") String backAction,
                                           @GuiParam(value = "evidenceId") int evidenceId,
                                           @GuiParam(value = "evidenceType") String evidenceType,
                                           @GuiParam(value = "evidenceDescription") String evidenceDescription) {
        return async(() -> investigationLinkEvidenceSelectionViewBuilder.buildGui(page, currentAction, backAction, new EvidenceDto(evidenceId, evidenceType, evidenceDescription)));
    }

    @GuiAction("manage-investigation-evidence/link")
    public void linkEvidence(Player player,
                             @GuiParam("investigationId") int investigationId,
                             @GuiParam(value = "evidenceId") int evidenceId,
                             @GuiParam(value = "evidenceType") String evidenceType,
                             @GuiParam(value = "evidenceDescription") String evidenceDescription) {
        bukkitUtils.runTaskAsync(player, () -> {
            Investigation investigation = investigationService.getInvestigation(investigationId);
            investigationEvidenceService.linkEvidence(player, investigation, new EvidenceDto(evidenceId, evidenceType, evidenceDescription));
        });
    }
}
