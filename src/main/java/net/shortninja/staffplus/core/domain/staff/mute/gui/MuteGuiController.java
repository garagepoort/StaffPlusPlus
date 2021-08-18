package net.shortninja.staffplus.core.domain.staff.mute.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.AsyncGui;
import be.garagepoort.mcioc.gui.CurrentAction;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mute.Mute;
import net.shortninja.staffplus.core.domain.staff.mute.MuteService;
import net.shortninja.staffplus.core.domain.staff.mute.gui.views.ManageMutedPlayerViewBuilder;
import net.shortninja.staffplus.core.domain.staff.mute.gui.views.ActiveMutedPlayersViewBuilder;
import net.shortninja.staffplus.core.domain.staff.mute.gui.views.PlayerMuteHistoryViewBuilder;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

import static be.garagepoort.mcioc.gui.AsyncGui.async;

@IocBean
@GuiController
public class MuteGuiController {

    private static final String CANCEL = "cancel";

    private final PlayerMuteHistoryViewBuilder playerMuteHistoryViewBuilder;
    private final ActiveMutedPlayersViewBuilder activeMutedPlayersViewBuilder;
    private final ManageMutedPlayerViewBuilder manageMutedPlayerViewBuilder;
    private final Messages messages;
    private final MuteService muteService;
    private final OnlineSessionsManager sessionManager;
    private final BukkitUtils bukkitUtils;
    private final PlayerManager playerManager;

    public MuteGuiController(PlayerMuteHistoryViewBuilder playerMuteHistoryViewBuilder,
                             ActiveMutedPlayersViewBuilder activeMutedPlayersViewBuilder,
                             ManageMutedPlayerViewBuilder manageMutedPlayerViewBuilder,
                             Messages messages,
                             MuteService muteService,
                             OnlineSessionsManager sessionManager,
                             BukkitUtils bukkitUtils,
                             PlayerManager playerManager) {
        this.playerMuteHistoryViewBuilder = playerMuteHistoryViewBuilder;
        this.activeMutedPlayersViewBuilder = activeMutedPlayersViewBuilder;
        this.manageMutedPlayerViewBuilder = manageMutedPlayerViewBuilder;
        this.messages = messages;
        this.muteService = muteService;
        this.sessionManager = sessionManager;
        this.bukkitUtils = bukkitUtils;
        this.playerManager = playerManager;
    }

    @GuiAction("manage-mutes/view/all-active")
    public AsyncGui<TubingGui> getMutedPlayersOverview(@GuiParam(value = "page", defaultValue = "0") int page,
                                                       @CurrentAction String currentAction,
                                                       @GuiParam("backAction") String backAction) {
        return async(() -> activeMutedPlayersViewBuilder.buildGui(page, currentAction, backAction));
    }

    @GuiAction("manage-mutes/view/history")
    public AsyncGui<TubingGui> getMutedPlayersOverview(@GuiParam(value = "page", defaultValue = "0") int page,
                                                       @CurrentAction String currentAction,
                                                       @GuiParam("targetPlayerName") String targetPlayerName,
                                                       @GuiParam("backAction") String backAction) {
        SppPlayer target = playerManager.getOnOrOfflinePlayer(targetPlayerName).orElseThrow(() -> new PlayerNotFoundException(targetPlayerName));
        return async(() -> playerMuteHistoryViewBuilder.buildGui(target, page, currentAction, backAction));
    }

    @GuiAction("manage-mutes/view/detail")
    public AsyncGui<TubingGui> getMuteDetailView(@GuiParam("muteId") int muteId,
                                                 @CurrentAction String currentAction,
                                                 @GuiParam("backAction") String backAction) {
        return async(() -> {
            Mute mute = muteService.getById(muteId);
            return manageMutedPlayerViewBuilder.buildGui(mute, currentAction, backAction);
        });
    }

    @GuiAction("manage-mutes/unmute")
    public void unmute(Player player, @GuiParam("muteId") int muteId) {
        messages.send(player, "&1=====================================================", messages.prefixGeneral);
        messages.send(player, "&6         You have chosen to unmute this player", messages.prefixGeneral);
        messages.send(player, "&6Type your reason for unmuting this player in chat", messages.prefixGeneral);
        messages.send(player, "&6        Type \"cancel\" to cancel the unmute ", messages.prefixGeneral);
        messages.send(player, "&1=====================================================", messages.prefixGeneral);

        OnlinePlayerSession playerSession = sessionManager.get(player);
        playerSession.setChatAction((player1, message) -> {
            if (message.equalsIgnoreCase(CANCEL)) {
                messages.send(player, "&CYou have cancelled unmuting this player", messages.prefixReports);
                return;
            }
            bukkitUtils.runTaskAsync(player1, () -> muteService.unmute(player, muteId, message));
        });
    }

}