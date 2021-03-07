package net.shortninja.staffplus.staff.reporting.cmd;

import net.shortninja.staffplus.common.JavaUtils;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.staff.reporting.ReportFilters.ReportFiltersBuilder;
import net.shortninja.staffplusplus.reports.ReportStatus;

import java.util.Arrays;
import java.util.List;

public class ReportFiltersMapper {

    private static final String ID = "id";
    private static final String REPORTER = "reporter";
    private static final String ASSIGNEE = "assignee";
    private static final String STATUS = "status";
    private static final String CULPRIT = "culprit";

    private final PlayerManager playerManager;

    public ReportFiltersMapper(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }


    public List<String> getFilterKeys() {
        return Arrays.asList(ID, REPORTER, ASSIGNEE, CULPRIT, STATUS);
    }

    public void map(String key, String value, ReportFiltersBuilder reportFiltersBuilder) {
        if (key.equalsIgnoreCase(ID)) {
            reportFiltersBuilder.id(Integer.parseInt(value));
        }
        if (key.equalsIgnoreCase(REPORTER)) {
            SppPlayer reporter = playerManager.getOnOrOfflinePlayer(value).orElseThrow(() -> new PlayerNotFoundException(value));
            reportFiltersBuilder.reporter(reporter);
        }
        if (key.equalsIgnoreCase(ASSIGNEE)) {
            SppPlayer assignee = playerManager.getOnOrOfflinePlayer(value).orElseThrow(() -> new PlayerNotFoundException(value));
            reportFiltersBuilder.assignee(assignee);
        }
        if (key.equalsIgnoreCase(STATUS)) {
            if (!JavaUtils.isValidEnum(ReportStatus.class, value.toUpperCase())) {
                throw new BusinessException("Invalid report status: [" + value + "]");
            }
            reportFiltersBuilder.reportStatus(ReportStatus.valueOf(value.toUpperCase()));
        }
        if (key.equalsIgnoreCase(CULPRIT)) {
            SppPlayer culprit = playerManager.getOnOrOfflinePlayer(value).orElseThrow(() -> new PlayerNotFoundException(value));
            reportFiltersBuilder.culprit(culprit);
        }
    }
}
