package net.shortninja.staffplus.core.domain.staff.reporting.actions;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.ConfiguredAction;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.reports.*;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

@IocBean
@IocListener
public class ReportActionsHook implements Listener {

    private final Options options;
    private final ActionService actionService;
    private final PlayerManager playerManager;

    public ReportActionsHook(Options options, ActionService actionService, PlayerManager playerManager) {
        this.options = options;
        this.actionService = actionService;
        this.playerManager = playerManager;
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

    private void executeActions(IReport report, List<ConfiguredAction> commands) {
        Optional<SppPlayer> reporter = playerManager.getOnlinePlayer(report.getReporterUuid());
        Optional<SppPlayer> assigned = playerManager.getOnlinePlayer(report.getStaffUuid());
        Optional<SppPlayer> culprit = report.getCulpritUuid() != null ? playerManager.getOnlinePlayer(report.getCulpritUuid()) : Optional.empty();

        Map<String, String> placeholders = new HashMap<>();
        reporter.ifPresent(sppPlayer -> placeholders.put("%reporter%", sppPlayer.getUsername()));
        assigned.ifPresent(sppPlayer -> placeholders.put("%assigned%", sppPlayer.getUsername()));
        culprit.ifPresent(sppPlayer -> placeholders.put("%culprit%", sppPlayer.getUsername()));

        ReportActionTargetProvider targetProvider = new ReportActionTargetProvider(reporter.orElse(null), assigned.orElse(null), culprit.orElse(null));
        actionService.executeActions(targetProvider, commands, Arrays.asList(new ReportTypeActionFilter(report), new ReportCulpritActionFilter(report)), placeholders);
    }

}
