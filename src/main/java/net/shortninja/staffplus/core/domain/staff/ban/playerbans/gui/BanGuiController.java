package net.shortninja.staffplus.core.domain.staff.ban.playerbans.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.CurrentAction;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.Ban;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanService;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.gui.views.BannedPlayersViewBuilder;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.gui.views.ManageBannedPlayerViewBuilder;
import org.bukkit.entity.Player;

@IocBean
@GuiController
public class BanGuiController {

    private static final String CANCEL = "cancel";

    private final BannedPlayersViewBuilder bannedPlayersViewBuilder;
    private final ManageBannedPlayerViewBuilder manageBannedPlayerViewBuilder;
    private final Messages messages;
    private final BanService banService;
    private final SessionManagerImpl sessionManager;

    public BanGuiController(BannedPlayersViewBuilder bannedPlayersViewBuilder, ManageBannedPlayerViewBuilder manageBannedPlayerViewBuilder, Messages messages, BanService banService, SessionManagerImpl sessionManager) {
        this.bannedPlayersViewBuilder = bannedPlayersViewBuilder;
        this.manageBannedPlayerViewBuilder = manageBannedPlayerViewBuilder;
        this.messages = messages;
        this.banService = banService;
        this.sessionManager = sessionManager;
    }

    @GuiAction("manage-bans/view/overview")
    public TubingGui getBannedPlayersOverview(@GuiParam(value = "page", defaultValue = "0") int page,
                                              @CurrentAction String currentAction,
                                              @GuiParam("backAction") String backAction) {
        return bannedPlayersViewBuilder.buildGui(page, currentAction, backAction);
    }

    @GuiAction("manage-bans/view/detail")
    public TubingGui getBanDetailView(@GuiParam("banId") int banId, @GuiParam("backAction") String backAction, @CurrentAction String currentAction) {
        Ban ban = banService.getById(banId);
        return manageBannedPlayerViewBuilder.buildGui(ban, backAction, currentAction);
    }

    @GuiAction("manage-bans/unban")
    public void unban(Player player, @GuiParam("banId") int banId) {
        messages.send(player, "&1=====================================================", messages.prefixGeneral);
        messages.send(player, "&6         You have chosen to unban this player", messages.prefixGeneral);
        messages.send(player, "&6Type your reason for unbanning this player in chat", messages.prefixGeneral);
        messages.send(player, "&6        Type \"cancel\" to cancel the unban ", messages.prefixGeneral);
        messages.send(player, "&1=====================================================", messages.prefixGeneral);

        PlayerSession playerSession = sessionManager.get(player.getUniqueId());
        playerSession.setChatAction((player1, message) -> {
            if (message.equalsIgnoreCase(CANCEL)) {
                messages.send(player, "&CYou have cancelled unbanning this player", messages.prefixReports);
                return;
            }
            banService.unban(player, banId, message);
        });
    }

}