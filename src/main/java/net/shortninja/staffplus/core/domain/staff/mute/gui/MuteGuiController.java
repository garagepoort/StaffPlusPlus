package net.shortninja.staffplus.core.domain.staff.mute.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.AsyncGui;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.templates.GuiTemplate;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mute.Mute;
import net.shortninja.staffplus.core.domain.staff.mute.MuteService;
import net.shortninja.staffplus.core.domain.staff.mute.database.MuteRepository;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static be.garagepoort.mcioc.gui.AsyncGui.async;
import static be.garagepoort.mcioc.gui.templates.GuiTemplate.template;

@IocBean
@GuiController
public class MuteGuiController {

    private static final String CANCEL = "cancel";
    private static final int PAGE_SIZE = 45;

    private final Messages messages;
    private final MuteService muteService;
    private final OnlineSessionsManager sessionManager;
    private final BukkitUtils bukkitUtils;
    private final PlayerManager playerManager;
    private final MuteRepository muteRepository;

    public MuteGuiController(Messages messages,
                             MuteService muteService,
                             OnlineSessionsManager sessionManager,
                             BukkitUtils bukkitUtils,
                             PlayerManager playerManager, MuteRepository muteRepository) {
        this.messages = messages;
        this.muteService = muteService;
        this.sessionManager = sessionManager;
        this.bukkitUtils = bukkitUtils;
        this.playerManager = playerManager;
        this.muteRepository = muteRepository;
    }

    @GuiAction("manage-mutes/view/all-active")
    public AsyncGui<GuiTemplate> getMutedPlayersOverview(@GuiParam(value = "page", defaultValue = "0") int page) {
        return async(() -> {
            List<Mute> allPaged = muteService.getAllPaged(page * PAGE_SIZE, PAGE_SIZE);
            Map<String, Object> params = new HashMap<>();
            params.put("title", "&bActive mutes");
            params.put("mutes", allPaged);
            params.put("guiId", "active-mutes-overview");
            return template("gui/mutes/mute-overview.ftl", params);
        });
    }

    @GuiAction("manage-mutes/view/history")
    public AsyncGui<GuiTemplate> getMutedPlayersOverview(@GuiParam(value = "page", defaultValue = "0") int page,
                                                         @GuiParam("targetPlayerName") String targetPlayerName) {
        SppPlayer target = playerManager.getOnOrOfflinePlayer(targetPlayerName).orElseThrow(() -> new PlayerNotFoundException(targetPlayerName));
        return async(() -> {
            List<Mute> allPaged = muteRepository.getMutesForPlayerPaged(target.getId(), page * PAGE_SIZE, PAGE_SIZE);
            Map<String, Object> params = new HashMap<>();
            params.put("title", "Mute History for: &C" + target.getUsername());
            params.put("mutes", allPaged);
            params.put("guiId", "history-mutes-overview");
            return template("gui/mutes/mute-overview.ftl", params);
        });
    }

    @GuiAction("manage-mutes/view/detail")
    public AsyncGui<GuiTemplate> getMuteDetailView(@GuiParam("muteId") int muteId) {
        return async(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("mute", muteService.getById(muteId));
            return template("gui/mutes/manage-mute.ftl", params);
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