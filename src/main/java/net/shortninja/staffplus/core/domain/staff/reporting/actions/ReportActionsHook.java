package net.shortninja.staffplus.core.domain.staff.reporting.actions;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommandMapper;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.reports.AcceptReportEvent;
import net.shortninja.staffplusplus.reports.IReport;
import net.shortninja.staffplusplus.reports.RejectReportEvent;
import net.shortninja.staffplusplus.reports.ReopenReportEvent;
import net.shortninja.staffplusplus.reports.ResolveReportEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@IocBean
@IocListener
public class ReportActionsHook implements Listener {

    private final Options options;
    private final ActionService actionService;
    private final PlayerManager playerManager;
    private final ConfiguredCommandMapper configuredCommandMapper;
    private final BukkitUtils bukkitUtils;

    public ReportActionsHook(Options options, ActionService actionService, PlayerManager playerManager, ConfiguredCommandMapper configuredCommandMapper, BukkitUtils bukkitUtils) {
        this.options = options;
        this.actionService = actionService;
        this.playerManager = playerManager;
        this.configuredCommandMapper = configuredCommandMapper;
        this.bukkitUtils = bukkitUtils;
    }

    @EventHandler
    public void onAccept(AcceptReportEvent event) {
        executeActions(event.getReport(), options.reportConfiguration.getAcceptReportActions());
    }


    @EventHandler
    public void onReject(RejectReportEvent event) {
        executeActions(event.getReport(), options.reportConfiguration.getRejectReportActions());
    }


    @EventHandler
    public void onReopen(ReopenReportEvent event) {
        executeActions(event.getReport(), options.reportConfiguration.getReopenReportActions());
    }


    @EventHandler
    public void onResolve(ResolveReportEvent event) {
        executeActions(event.getReport(), options.reportConfiguration.getResolveReportActions());
    }

    private void executeActions(IReport report, List<ConfiguredCommand> commands) {
        bukkitUtils.runTaskAsync(() -> {
            Optional<SppPlayer> reporter = playerManager.getOnOrOfflinePlayer(report.getReporterUuid());
            Optional<SppPlayer> assigned = playerManager.getOnOrOfflinePlayer(report.getStaffUuid());
            Optional<SppPlayer> culprit = report.getCulpritUuid() != null ? playerManager.getOnOrOfflinePlayer(report.getCulpritUuid()) : Optional.empty();

            Map<String, String> placeholders = new HashMap<>();
            reporter.ifPresent(sppPlayer -> placeholders.put("%reporter%", sppPlayer.getUsername()));
            assigned.ifPresent(sppPlayer -> placeholders.put("%assigned%", sppPlayer.getUsername()));
            culprit.ifPresent(sppPlayer -> placeholders.put("%culprit%", sppPlayer.getUsername()));

            Map<String, OfflinePlayer> targets = new HashMap<>();
            reporter.ifPresent(sppPlayer -> targets.put("reporter", sppPlayer.getOfflinePlayer()));
            assigned.ifPresent(sppPlayer -> targets.put("assigned", sppPlayer.getOfflinePlayer()));
            culprit.ifPresent(sppPlayer -> targets.put("culprit", sppPlayer.getOfflinePlayer()));

            actionService.createCommands(configuredCommandMapper.toCreateRequests(commands, placeholders, targets, Arrays.asList(new ReportTypeActionFilter(report), new ReportCulpritActionFilter(report))));
        });
    }

}
