package net.shortninja.staffplus.core.domain.staff.mute.gui.views;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.gui.PagedGuiBuilder;
import net.shortninja.staffplus.core.domain.staff.mute.Mute;
import net.shortninja.staffplus.core.domain.staff.mute.database.MuteRepository;
import net.shortninja.staffplusplus.session.SppPlayer;

import java.util.List;

@IocBean
public class PlayerMuteHistoryViewBuilder {
    private static final int PAGE_SIZE = 45;

    private final MuteRepository muteRepository;
    private final Messages messages;
    private final MutedPlayerItemBuilder mutedPlayerItemBuilder;

    public PlayerMuteHistoryViewBuilder(MuteRepository muteRepository, Messages messages, MutedPlayerItemBuilder mutedPlayerItemBuilder) {
        this.muteRepository = muteRepository;
        this.messages = messages;
        this.mutedPlayerItemBuilder = mutedPlayerItemBuilder;
    }

    public TubingGui buildGui(SppPlayer target, int page, String currentAction, String backAction) {
        List<Mute> allPaged = muteRepository.getMutesForPlayerPaged(target.getId(), page * PAGE_SIZE, PAGE_SIZE);

        return new PagedGuiBuilder.Builder(messages.colorize("Mute History for: &C" + target.getUsername()))
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
