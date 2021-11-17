package net.shortninja.staffplus.core.domain.staff.warn.warnings.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.model.TubingGui;
import net.shortninja.staffplus.core.common.gui.PagedGuiBuilder;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import net.shortninja.staffplusplus.session.SppPlayer;

import java.util.List;

@IocBean
public class ManageWarningsViewBuilder {

    private static final int PAGE_SIZE = 45;
    private final WarnService warnService;
    private final WarningItemBuilder warningItemBuilder;

    public ManageWarningsViewBuilder(WarnService warnService, WarningItemBuilder warningItemBuilder) {
        this.warnService = warnService;
        this.warningItemBuilder = warningItemBuilder;
    }

    public TubingGui buildGui(SppPlayer target, String currentAction, int page, String backAction) {
        return new PagedGuiBuilder.Builder("Manage warnings")
            .addPagedItems(currentAction, getItems(target, page), warningItemBuilder::build, w -> getDetailAction(currentAction, w), page)
            .backAction(backAction)
            .build();
    }

    private String getDetailAction(String currentAction, Warning w) {
        return GuiActionBuilder.builder()
            .action("manage-warnings/view/detail")
            .param("warningId", String.valueOf(w.getId()))
            .param("backAction", currentAction)
            .build();
    }

    public List<Warning> getItems(SppPlayer target, int page) {
        if (target == null) {
            return warnService.getAllWarnings(page * PAGE_SIZE, PAGE_SIZE, true);
        }
        return warnService.getWarnings(target.getId(), page * PAGE_SIZE, PAGE_SIZE, true);
    }
}