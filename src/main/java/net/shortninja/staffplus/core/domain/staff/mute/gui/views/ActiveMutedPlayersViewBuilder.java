package net.shortninja.staffplus.core.domain.staff.mute.gui.views;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.gui.PagedGuiBuilder;
import net.shortninja.staffplus.core.domain.staff.mute.Mute;
import net.shortninja.staffplus.core.domain.staff.mute.MuteService;

import java.util.List;

@IocBean
public class ActiveMutedPlayersViewBuilder {
    private static final int PAGE_SIZE = 45;

    private final MuteService muteService;
    private final Messages messages;
    private final MutedPlayerItemBuilder mutedPlayerItemBuilder;

    public ActiveMutedPlayersViewBuilder(MuteService muteService, Messages messages, MutedPlayerItemBuilder mutedPlayerItemBuilder) {
        this.muteService = muteService;
        this.messages = messages;
        this.mutedPlayerItemBuilder = mutedPlayerItemBuilder;
    }

    public TubingGui buildGui(int page, String currentAction, String backAction) {
        List<Mute> allPaged = muteService.getAllPaged(page * PAGE_SIZE, PAGE_SIZE);

        return new PagedGuiBuilder.Builder(messages.colorize("Manage Mutes"))
            .addPagedItems(currentAction, allPaged, mutedPlayerItemBuilder::build, b -> getDetailAction(currentAction, b), page)
            .backAction(backAction)
            .build();
    }

    private String getDetailAction(String backToOverviewAction, Mute mute) {
        return GuiActionBuilder.builder()
            .action("manage-mutes/view/detail")
            .param("muteId", String.valueOf(mute.getId()))
            .param("backAction", backToOverviewAction)
            .build();
    }

}
