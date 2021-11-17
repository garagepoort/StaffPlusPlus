package net.shortninja.staffplus.core.domain.staff.warn.warnings.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.model.TubingGui;
import net.shortninja.staffplus.core.common.gui.PagedGuiBuilder;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@IocBean
public class ManageAppealedWarningsViewBuilder {

    private static final int PAGE_SIZE = 45;
    private final WarnService warnService;
    private final WarningItemBuilder warningItemBuilder;

    public ManageAppealedWarningsViewBuilder(WarnService warnService, WarningItemBuilder warningItemBuilder) {
        this.warnService = warnService;
        this.warningItemBuilder = warningItemBuilder;
    }

    public TubingGui buildGui(String currentAction, int page) {
        return new PagedGuiBuilder.Builder("Appealed warnings")
            .addPagedItems(currentAction, getItems(page * PAGE_SIZE, PAGE_SIZE), warningItemBuilder::build, w -> getDetailAction(w, currentAction), page)
            .build();
    }

    @NotNull
    private String getDetailAction(Warning w, String currentAction) {
        return GuiActionBuilder.builder()
            .action("manage-warnings/view/detail")
            .param("warningId", String.valueOf(w.getId()))
            .param("backAction", currentAction)
            .build();
    }

    public List<Warning> getItems(int offset, int amount) {
        return warnService.getAppealedWarnings(offset, amount);
    }
}