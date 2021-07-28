package net.shortninja.staffplus.core.domain.staff.investigate.gui.views;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.common.gui.PagedGuiBuilder;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import net.shortninja.staffplusplus.session.SppPlayer;

import java.util.List;

@IocBean
public class InvestigationsOverviewViewBuilder {

    private static final int PAGE_SIZE = 45;

    private final InvestigationService investigationService;
    private final InvestigationItemBuilder investigationItemBuilder;

    public InvestigationsOverviewViewBuilder(InvestigationService investigationService, InvestigationItemBuilder investigationItemBuilder) {
        this.investigationService = investigationService;
        this.investigationItemBuilder = investigationItemBuilder;
    }

    public TubingGui buildGui(SppPlayer target, int page, String backAction, String currentAction) {
        return new PagedGuiBuilder.Builder("Manage Investigation")
            .addPagedItems(currentAction, getInvestigations(target, page * PAGE_SIZE, PAGE_SIZE), investigationItemBuilder::build, i -> getDetailAction(currentAction, i), page)
            .backAction(backAction)
            .build();
    }

    public List<Investigation> getInvestigations(SppPlayer target, int offset, int amount) {
        if (target == null) {
            return investigationService.getAllInvestigations(offset, amount);
        }
        return investigationService.getInvestigationsForInvestigated(target, offset, amount);
    }

    private String getDetailAction(String backToOverviewAction, Investigation investigation) {
        return GuiActionBuilder.builder()
            .action("manage-investigations/view/detail")
            .param("investigationId", String.valueOf(investigation.getId()))
            .param("backAction", backToOverviewAction)
            .build();
    }

}