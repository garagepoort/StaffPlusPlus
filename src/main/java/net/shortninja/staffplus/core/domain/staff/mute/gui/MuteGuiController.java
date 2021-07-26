package net.shortninja.staffplus.core.domain.staff.mute.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.CurrentAction;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.domain.staff.mute.Mute;
import net.shortninja.staffplus.core.domain.staff.mute.MuteService;
import net.shortninja.staffplus.core.domain.staff.mute.gui.views.ManageMutedPlayerViewBuilder;
import net.shortninja.staffplus.core.domain.staff.mute.gui.views.MutedPlayersViewBuilder;
import org.bukkit.entity.Player;

@IocBean
@GuiController
public class MuteGuiController {

    private static final String CANCEL = "cancel";

    private final MutedPlayersViewBuilder mutedPlayersViewBuilder;
    private final ManageMutedPlayerViewBuilder manageMutedPlayerViewBuilder;
    private final Messages messages;
    private final MuteService muteService;
    private final SessionManagerImpl sessionManager;

    public MuteGuiController(MutedPlayersViewBuilder mutedPlayersViewBuilder, ManageMutedPlayerViewBuilder manageMutedPlayerViewBuilder, Messages messages, MuteService muteService, SessionManagerImpl sessionManager) {
        this.mutedPlayersViewBuilder = mutedPlayersViewBuilder;
        this.manageMutedPlayerViewBuilder = manageMutedPlayerViewBuilder;
        this.messages = messages;
        this.muteService = muteService;
        this.sessionManager = sessionManager;
    }

    @GuiAction("manage-mutes/view/overview")
    public TubingGui getMutedPlayersOverview(Player player, @GuiParam(value = "page", defaultValue = "0") int page, @GuiParam("backAction") String backAction) {
        return mutedPlayersViewBuilder.buildGui(page, backAction);
    }

    @GuiAction("manage-mutes/view/detail")
    public TubingGui getMuteDetailView(@GuiParam("muteId") int muteId,
                                       @GuiParam("backAction") String backAction,
                                       @CurrentAction String currentAction) {
        Mute mute = muteService.getById(muteId);
        return manageMutedPlayerViewBuilder.buildGui(mute, backAction, currentAction);
    }

    @GuiAction("manage-mutes/unmute")
    public void unmute(Player player, @GuiParam("muteId") int muteId) {
        messages.send(player, "&1=====================================================", messages.prefixGeneral);
        messages.send(player, "&6         You have chosen to unmute this player", messages.prefixGeneral);
        messages.send(player, "&6Type your reason for unmuting this player in chat", messages.prefixGeneral);
        messages.send(player, "&6        Type \"cancel\" to cancel the unmute ", messages.prefixGeneral);
        messages.send(player, "&1=====================================================", messages.prefixGeneral);

        PlayerSession playerSession = sessionManager.get(player.getUniqueId());
        playerSession.setChatAction((player1, message) -> {
            if (message.equalsIgnoreCase(CANCEL)) {
                messages.send(player, "&CYou have cancelled unmuting this player", messages.prefixReports);
                return;
            }
            muteService.unmute(player, muteId, message);
            sessionManager.get(player.getUniqueId()).setMuted(false);
        });
    }

}