package net.shortninja.staffplus.core.domain.staff.infractions.gui.views;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.model.TubingGui;
import be.garagepoort.mcioc.gui.model.TubingGuiActions;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.gui.PagedGuiBuilder;
import net.shortninja.staffplus.core.domain.staff.infractions.Infraction;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionsService;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

import java.util.List;

@IocBean
public class InfractionsOverviewViewBuilder {

    private static final int PAGE_SIZE = 45;

    public TubingGui buildGui(Player player, SppPlayer target, int page, String currentAction) {

        return new PagedGuiBuilder.Builder("Infractions " + target.getUsername())
            .addPagedItems(currentAction, getItems(player, target, page), i -> {
                InfractionGuiProvider infractionGuiProvider = getInfractionGuiProvider(i);
                return infractionGuiProvider.getMenuItem(i);
            }, i -> TubingGuiActions.NOOP, page)
            .build();
    }

    public List<Infraction> getItems(Player player, SppPlayer target, int currentPage) {
        return StaffPlus.get().getIocContainer().get(InfractionsService.class)
            .getAllInfractions(player, target.getId(), currentPage * PAGE_SIZE, PAGE_SIZE);
    }

    private InfractionGuiProvider getInfractionGuiProvider(Infraction i) {
        return StaffPlus.get().getIocContainer().getList(InfractionGuiProvider.class).stream()
            .filter(guiProvider -> guiProvider.getType().equals(i.getInfractionType())).findFirst()
            .orElseThrow(() -> new RuntimeException("No gui provider for infraction type: [" + i.getInfractionType() + "]"));
    }
}