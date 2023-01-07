package net.shortninja.staffplus.core.domain.staff.reporting.gui;

import be.garagepoort.mcioc.tubinggui.GuiAction;
import be.garagepoort.mcioc.tubinggui.GuiController;
import be.garagepoort.mcioc.tubinggui.GuiParam;
import be.garagepoort.mcioc.tubinggui.templates.GuiTemplate;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportService;
import net.shortninja.staffplus.core.domain.staff.reporting.config.CulpritFilterPredicate;
import net.shortninja.staffplus.core.domain.staff.reporting.config.ManageReportConfiguration;
import net.shortninja.staffplus.core.domain.staff.reporting.config.ReportConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static be.garagepoort.mcioc.tubinggui.templates.GuiTemplate.template;
import static net.shortninja.staffplus.core.application.SppInteractorBuilder.fromSender;

@GuiController
public class CreateReportsGuiController {

    private final PermissionHandler permissionHandler;
    private final ManageReportConfiguration manageReportConfiguration;
    private final ReportService reportService;
    private final BukkitUtils bukkitUtils;
    private final PlayerManager playerManager;
    private final ReportConfiguration reportConfiguration;

    public CreateReportsGuiController(PermissionHandler permissionHandler,
                                      ManageReportConfiguration manageReportConfiguration,
                                      ReportService reportService,
                                      BukkitUtils bukkitUtils,
                                      PlayerManager playerManager,
                                      ReportConfiguration reportConfiguration) {
        this.permissionHandler = permissionHandler;
        this.manageReportConfiguration = manageReportConfiguration;
        this.reportService = reportService;
        this.bukkitUtils = bukkitUtils;
        this.playerManager = playerManager;
        this.reportConfiguration = reportConfiguration;
    }

    @GuiAction("reports/view/type-select")
    public GuiTemplate showTypeSelect(Player player,
                                      @GuiParam("culprit") String culprit,
                                      @GuiParam("reason") String reason) {
        permissionHandler.validate(player, manageReportConfiguration.permissionView);
        Map<String, Object> params = new HashMap<>();
        params.put("types", reportConfiguration.getReportTypeConfigurations(new CulpritFilterPredicate(StringUtils.isNotEmpty(culprit))));
        params.put("skipReasonSelect", StringUtils.isNotEmpty(reason));
        return template("gui/reports/report-type-selection.ftl", params);
    }

    @GuiAction("reports/view/reason-select")
    public GuiTemplate showReasonSelect(Player player,
                                        @GuiParam("culprit") String culprit,
                                        @GuiParam("type") String type) {
        permissionHandler.validate(player, manageReportConfiguration.permissionView);
        Map<String, Object> params = new HashMap<>();
        params.put("reasons", reportConfiguration
            .getReportReasonConfigurations(new CulpritFilterPredicate(StringUtils.isNotEmpty(culprit)))
            .stream()
            .filter(r -> type == null || (r.getReportType().isPresent() && r.getReportType().get().equals(type)))
            .collect(Collectors.toList()));
        return template("gui/reports/report-reason-selection.ftl", params);
    }

    @GuiAction("reports/create")
    public void createReport(Player player,
                             @GuiParam("culprit") String culprit,
                             @GuiParam("reason") String reportReason,
                             @GuiParam("type") String reportType) {
        bukkitUtils.runTaskAsync(player, () -> {
            if (StringUtils.isNotEmpty(culprit)) {
                SppPlayer culpritPlayer = playerManager.getOnOrOfflinePlayer(culprit).orElseThrow(() -> new PlayerNotFoundException(culprit));
                reportService.sendReport(fromSender(player), culpritPlayer, reportReason, reportType);
            } else {
                reportService.sendReport(fromSender(player), reportReason, reportType);
            }
        });
    }
}