package net.shortninja.staffplus.core.domain.player.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.templates.GuiTemplate;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.gui.model.PlayerOverviewModel;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanService;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.config.BanConfiguration;
import net.shortninja.staffplus.core.domain.staff.mute.MuteService;
import net.shortninja.staffplus.core.domain.staff.mute.config.MuteConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;

@IocBean
@GuiController
public class PlayersGuiController {

    private final PlayerManager playerManager;
    private final BanService banService;
    private final BanConfiguration banConfiguration;
    private final MuteService muteService;
    private final MuteConfiguration muteConfiguration;

    public PlayersGuiController(PlayerManager playerManager, BanService banService, BanConfiguration banConfiguration, MuteService muteService, MuteConfiguration muteConfiguration) {
        this.playerManager = playerManager;
        this.banService = banService;
        this.banConfiguration = banConfiguration;
        this.muteService = muteService;
        this.muteConfiguration = muteConfiguration;
    }

    @GuiAction("players/view/detail")
    public GuiTemplate getItems(Player staff, @GuiParam(value = "targetPlayerName") String playerName) {
        SppPlayer sppPlayer = playerManager.getOnOrOfflinePlayer(playerName).orElseThrow(() -> new PlayerNotFoundException(playerName));
        HashMap<String, Object> params = new HashMap<>();
        params.put("model", new PlayerOverviewModel(
            banConfiguration.enabled ? banService.getBanByBannedUuid(sppPlayer.getId()) : Optional.empty(),
            muteConfiguration.muteEnabled ? muteService.getMuteByMutedUuid(sppPlayer.getId()) : Optional.empty()
        ));
        params.put("player", sppPlayer);
        return GuiTemplate.template("gui/player/player-detail.ftl", params);
    }
}
