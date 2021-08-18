package net.shortninja.staffplus.core.domain.player.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.templates.GuiTemplate;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.gui.model.PlayerOverviewModel;
import net.shortninja.staffplus.core.domain.player.ip.database.PlayerIpRepository;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBan;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBanConfiguration;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBanService;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanService;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.config.BanConfiguration;
import net.shortninja.staffplus.core.domain.staff.mute.MuteService;
import net.shortninja.staffplus.core.domain.staff.mute.config.MuteConfiguration;
import net.shortninja.staffplus.core.domain.staff.reporting.Report;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.warnings.IWarning;
import net.shortninja.staffplusplus.warnings.WarningFilters;
import net.shortninja.staffplusplus.warnings.WarningService;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static net.shortninja.staffplus.core.common.utils.BukkitUtils.getIpFromPlayer;

@IocBean
@GuiController
public class PlayersGuiController {

    @ConfigProperty("reports-module.enabled")
    private boolean reportsEnabled;

    private final PlayerManager playerManager;
    private final BanService banService;
    private final IpBanService ipBanService;
    private final BanConfiguration banConfiguration;
    private final IpBanConfiguration ipBanConfiguration;
    private final MuteService muteService;
    private final MuteConfiguration muteConfiguration;
    private final PlayerIpRepository playerIpRepository;
    private final ReportService reportService;
    private final WarningService warningService;
    private final WarningConfiguration warningConfiguration;

    public PlayersGuiController(PlayerManager playerManager,
                                BanService banService,
                                IpBanService ipBanService,
                                BanConfiguration banConfiguration,
                                IpBanConfiguration ipBanConfiguration,
                                MuteService muteService,
                                MuteConfiguration muteConfiguration,
                                PlayerIpRepository playerIpRepository,
                                ReportService reportService,
                                WarningService warningService,
                                WarningConfiguration warningConfiguration) {
        this.playerManager = playerManager;
        this.banService = banService;
        this.ipBanService = ipBanService;
        this.banConfiguration = banConfiguration;
        this.ipBanConfiguration = ipBanConfiguration;
        this.muteService = muteService;
        this.muteConfiguration = muteConfiguration;
        this.playerIpRepository = playerIpRepository;
        this.reportService = reportService;
        this.warningService = warningService;
        this.warningConfiguration = warningConfiguration;
    }

    @GuiAction("players/view/detail")
    public GuiTemplate getItems(Player staff, @GuiParam(value = "targetPlayerName") String playerName) {
        SppPlayer sppPlayer = playerManager.getOnOrOfflinePlayer(playerName).orElseThrow(() -> new PlayerNotFoundException(playerName));
        HashMap<String, Object> params = new HashMap<>();


        params.put("target", sppPlayer);
        params.put("model", new PlayerOverviewModel(
            banConfiguration.enabled ? banService.getBanByBannedUuid(sppPlayer.getId()) : Optional.empty(),
            muteConfiguration.muteEnabled ? muteService.getMuteByMutedUuid(sppPlayer.getId()) : Optional.empty(),
            ipBanConfiguration.enabled ? getIpBans(sppPlayer) : emptyList(),
            reportsEnabled ? getReports(sppPlayer) : emptyList(),
            reportsEnabled ? getReported(sppPlayer) : emptyList(),
            warningConfiguration.isEnabled() ? getWarnings(sppPlayer) : emptyList()));
        return GuiTemplate.template("gui/player/player-detail.ftl", params);
    }

    private List<? extends IWarning> getWarnings(SppPlayer sppPlayer) {
        return warningService.findWarnings(new WarningFilters.WarningFiltersBuilder()
            .culprit(sppPlayer)
            .expired(false)
            .build(), 0, 1000);
    }

    private List<IpBan> getIpBans(SppPlayer sppPlayer) {
        Optional<String> ipAddress = sppPlayer.isOnline() ? Optional.of(getIpFromPlayer(sppPlayer.getPlayer())) : playerIpRepository.getLastIp(sppPlayer.getId());
        return ipAddress.isPresent() ? ipBanService.findMatchingIpBans(ipAddress.get()) : emptyList();
    }

    private List<Report> getReports(SppPlayer sppPlayer) {
        return reportService.getMyReports(sppPlayer.getId());
    }

    private List<Report> getReported(SppPlayer sppPlayer) {
        return reportService.getReported(sppPlayer.getId(), 0, 100);
    }
}
