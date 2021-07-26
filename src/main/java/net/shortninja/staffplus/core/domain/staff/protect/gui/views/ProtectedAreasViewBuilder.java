package net.shortninja.staffplus.core.domain.staff.protect.gui.views;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.gui.GuiItemConfig;
import net.shortninja.staffplus.core.common.gui.PagedGuiBuilder;
import net.shortninja.staffplus.core.domain.staff.protect.ProtectService;
import net.shortninja.staffplus.core.domain.staff.protect.ProtectedArea;

import java.util.List;

@IocBean
public class ProtectedAreasViewBuilder {

    private static final int PAGE_SIZE = 45;

    private final GuiItemConfig protectGuiItemConfig;
    private final ProtectService protectService;
    private final Messages messages;

    public ProtectedAreasViewBuilder(Options options, ProtectService protectService, Messages messages) {
        protectGuiItemConfig = options.protectConfiguration.getGuiItemConfig();
        this.protectService = protectService;
        this.messages = messages;
    }

    public TubingGui buildGui(int page, String backAction) {
        List<ProtectedArea> areas = protectService.getAllProtectedAreasPaginated(page * PAGE_SIZE, PAGE_SIZE);
        String backToOverviewAction = GuiActionBuilder.builder().action("protected-areas/view")
            .param("page", String.valueOf(page))
            .param("backAction", backAction)
            .build();

        return new PagedGuiBuilder.Builder(messages.colorize(protectGuiItemConfig.getTitle()))
            .addPagedItems("protected-areas/view", areas, ProtectedAreaItemBuilder::build, a -> getDetailAction(backToOverviewAction, a), page, PAGE_SIZE)
            .backAction(backAction)
            .build();
    }

    private String getDetailAction(String backToOverviewAction, ProtectedArea a) {
        return GuiActionBuilder.builder()
            .action("protected-areas/view/detail")
            .param("areaId", String.valueOf(a.getId()))
            .param("backAction", backToOverviewAction)
            .build();
    }


}
