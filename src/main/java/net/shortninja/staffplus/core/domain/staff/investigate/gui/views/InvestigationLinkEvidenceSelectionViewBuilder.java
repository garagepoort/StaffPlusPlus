package net.shortninja.staffplus.core.domain.staff.investigate.gui.views;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.common.gui.PagedGuiBuilder;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import net.shortninja.staffplusplus.investigate.evidence.Evidence;

import java.util.List;

@IocBean
public class InvestigationLinkEvidenceSelectionViewBuilder {

    private static final int PAGE_SIZE = 45;

    private final InvestigationService investigationService;
    private final InvestigationItemBuilder investigationItemBuilder;

    public InvestigationLinkEvidenceSelectionViewBuilder(InvestigationService investigationService, InvestigationItemBuilder investigationItemBuilder) {
        this.investigationService = investigationService;
        this.investigationItemBuilder = investigationItemBuilder;
    }

    public TubingGui buildGui(int page, String currentAction, String backAction, Evidence evidence) {
        return new PagedGuiBuilder.Builder("Manage Investigation")
            .addPagedItems(currentAction, getInvestigations(page * PAGE_SIZE, PAGE_SIZE), investigationItemBuilder::build, i -> GuiActionBuilder.builder()
                .action("manage-investigation-evidence/link")
                .param("investigationId", String.valueOf(i.getId()))
                .param("evidenceId", String.valueOf(evidence.getId()))
                .param("evidenceType", evidence.getEvidenceType())
                .param("evidenceDescription", evidence.getDescription())
                .build(), page)
            .backAction(backAction)
            .build();
    }

    public List<Investigation> getInvestigations(int offset, int amount) {
        return investigationService.getAllInvestigations(offset, amount);
    }

}