package net.shortninja.staffplus.core.domain.staff.infractions.gui.views;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.tubinggui.GuiActionBuilder;
import be.garagepoort.mcioc.tubinggui.model.TubingGui;
import net.shortninja.staffplus.core.common.gui.PagedGuiBuilder;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionOverview;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionType;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionsService;

import java.util.List;

@IocBean
public class InfractionsTopViewBuilder {

    private static final int PAGE_SIZE = 45;

    private final InfractionsService infractionsService;
    private final InfractionOverviewGuiProvider infractionOverviewGuiProvider;

    public InfractionsTopViewBuilder(InfractionsService infractionsService, InfractionOverviewGuiProvider infractionOverviewGuiProvider) {
        this.infractionsService = infractionsService;
        this.infractionOverviewGuiProvider = infractionOverviewGuiProvider;
    }

    public TubingGui buildGui(int page, List<InfractionType> infractionFilters, String currentAction) {
        return new PagedGuiBuilder.Builder("Infractions top")
            .addPagedItems(currentAction, getItems(page, infractionFilters),
                infractionOverviewGuiProvider::build, i -> getOverviewAction(currentAction, i), page)
            .build();
    }

    private String getOverviewAction(String currentAction, InfractionOverview i) {
        return GuiActionBuilder.builder()
            .action("manage-infractions/view/overview")
            .param("targetPlayerName", i.getSppPlayer().getUsername())
            .build();
    }

    public List<InfractionOverview> getItems(int page, List<InfractionType> infractionFilters) {
        return infractionsService.getTopInfractions(page, PAGE_SIZE, infractionFilters);
    }
}