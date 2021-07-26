package net.shortninja.staffplus.core.domain.staff.investigate.gui.evidence;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.domain.confirmation.ConfirmationViewBuilder;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationEvidenceService;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.views.EvidenceOverviewViewBuilder;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.views.InvestigationLinkEvidenceSelectionViewBuilder;
import org.bukkit.entity.Player;

@IocBean
@GuiController
public class InvestigationEvidenceGuiController {

    private final EvidenceOverviewViewBuilder evidenceOverviewViewBuilder;
    private final ConfirmationViewBuilder confirmationViewBuilder;
    private final InvestigationService investigationService;
    private final InvestigationEvidenceService investigationEvidenceService;
    private final InvestigationLinkEvidenceSelectionViewBuilder investigationLinkEvidenceSelectionViewBuilder;

    public InvestigationEvidenceGuiController(EvidenceOverviewViewBuilder evidenceOverviewViewBuilder, ConfirmationViewBuilder confirmationViewBuilder, InvestigationService investigationService, InvestigationEvidenceService investigationEvidenceService, InvestigationLinkEvidenceSelectionViewBuilder investigationLinkEvidenceSelectionViewBuilder) {
        this.evidenceOverviewViewBuilder = evidenceOverviewViewBuilder;
        this.confirmationViewBuilder = confirmationViewBuilder;
        this.investigationService = investigationService;
        this.investigationEvidenceService = investigationEvidenceService;
        this.investigationLinkEvidenceSelectionViewBuilder = investigationLinkEvidenceSelectionViewBuilder;
    }

    @GuiAction("manage-investigation-evidence/view")
    public TubingGui getEvidenceOverview(Player player, @GuiParam(value = "page", defaultValue = "0") int page, @GuiParam("investigationId") int investigationId, @GuiParam("backAction") String backAction) {
        Investigation investigation = investigationService.getInvestigation(investigationId);
        return evidenceOverviewViewBuilder.buildGui(player, investigation, page, backAction);
    }

    @GuiAction("manage-investigation-evidence/view/unlink")
    public TubingGui goToUnlinkEvidenceView(Player player, @GuiParam("evidenceId") int evidenceId, @GuiParam("investigationId") int investigationId, @GuiParam("backAction") String backAction) {
        String confirmAction = GuiActionBuilder.builder()
            .action("manage-investigation-evidence/unlink")
            .param("evidenceId", String.valueOf(evidenceId))
            .param("investigationId", String.valueOf(investigationId))
            .param("backAction", backAction)
            .build();

        return confirmationViewBuilder.buildGui("Unlink evidence?",
            "Are you sure you want to unlink evidence: (ID=" + evidenceId + ")",
            confirmAction,
            backAction
        );
    }

    @GuiAction("manage-investigation-evidence/unlink")
    public String unlinkEvidence(Player player, @GuiParam("evidenceId") int evidenceId, @GuiParam("investigationId") int investigationId, @GuiParam("backAction") String backAction) {
        Investigation investigation = investigationService.getInvestigation(investigationId);
        investigationEvidenceService.unlinkEvidence(player, investigation, evidenceId);
        return backAction;
    }

    @GuiAction("manage-investigation-evidence/view/investigation-link")
    public TubingGui getOverview(@GuiParam(value = "page", defaultValue = "0") int page,
                                 @GuiParam("backAction") String backAction,
                                 @GuiParam(value = "evidenceId") int evidenceId,
                                 @GuiParam(value = "evidenceType") String evidenceType,
                                 @GuiParam(value = "evidenceDescription") String evidenceDescription) {
        return investigationLinkEvidenceSelectionViewBuilder.buildGui(page, backAction, new EvidenceDto(evidenceId, evidenceType, evidenceDescription));
    }

    @GuiAction("manage-investigation-evidence/link")
    public void linkEvidence(Player player,
                             @GuiParam("investigationId") int investigationId,
                             @GuiParam(value = "evidenceId") int evidenceId,
                             @GuiParam(value = "evidenceType") String evidenceType,
                             @GuiParam(value = "evidenceDescription") String evidenceDescription) {
        Investigation investigation = investigationService.getInvestigation(investigationId);
        investigationEvidenceService.linkEvidence(player, investigation, new EvidenceDto(evidenceId, evidenceType, evidenceDescription));
    }
}
