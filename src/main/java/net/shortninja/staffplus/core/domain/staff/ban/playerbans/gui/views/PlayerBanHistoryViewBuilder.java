package net.shortninja.staffplus.core.domain.staff.ban.playerbans.gui.views;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.gui.PagedGuiBuilder;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.Ban;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.database.BansRepository;
import net.shortninja.staffplusplus.session.SppPlayer;

import java.util.List;

@IocBean
public class PlayerBanHistoryViewBuilder {
    private static final int PAGE_SIZE = 45;

    private final BansRepository bansRepository;
    private final Messages messages;
    private final BannedPlayerItemBuilder bannedPlayerItemBuilder;

    public PlayerBanHistoryViewBuilder(BansRepository bansRepository, Messages messages, BannedPlayerItemBuilder bannedPlayerItemBuilder) {
        this.bansRepository = bansRepository;
        this.messages = messages;
        this.bannedPlayerItemBuilder = bannedPlayerItemBuilder;
    }

    public TubingGui buildGui(SppPlayer target, int page, String currentAction, String backAction) {
        List<Ban> allPaged = bansRepository.getBansForPlayerPaged(target.getId(), page * PAGE_SIZE, PAGE_SIZE);

        return new PagedGuiBuilder.Builder(messages.colorize("Ban History for: &C" + target.getUsername()))
            .addPagedItems(currentAction, allPaged, bannedPlayerItemBuilder::build, b -> getDetailAction(currentAction, b), page)
            .backAction(backAction)
            .build();
    }

    private String getDetailAction(String backToOverviewAction, Ban ban) {
        return GuiActionBuilder.builder()
            .action("manage-bans/view/detail")
            .param("banId", String.valueOf(ban.getId()))
            .param("backAction", backToOverviewAction)
            .build();
    }

}
