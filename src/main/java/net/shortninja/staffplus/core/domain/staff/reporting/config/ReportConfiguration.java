package net.shortninja.staffplus.core.domain.staff.reporting.config;

import net.shortninja.staffplus.core.common.Sounds;
import net.shortninja.staffplus.core.common.gui.GuiItemConfig;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;
import net.shortninja.staffplusplus.reports.ReportStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ReportConfiguration {

    private final boolean enabled;
    private final int cooldown;
    private final boolean showReporter;
    private final Sounds sound;
    private final boolean closingReasonEnabled;
    private final GuiItemConfig openReportsGui;
    private final GuiItemConfig myReportsGui;
    private final GuiItemConfig assignedReportsGui;
    private final GuiItemConfig closedReportsGui;
    private final String myReportsPermission;
    private final String myReportsCmd;
    private final boolean notifyReporterOnJoin;
    private final List<ReportStatus> reporterNotifyStatuses;
    private final List<ReportTypeConfiguration> reportTypeConfigurations;
    private final boolean fixedReason;
    private final boolean fixedReasonCulprit;
    private final List<ReportReasonConfiguration> reportReasonConfigurations;
    private final List<ConfiguredCommand> acceptReportActions;
    private final List<ConfiguredCommand> rejectReportActions;
    private final List<ConfiguredCommand> reopenReportActions;
    private final List<ConfiguredCommand> resolveReportActions;


    public ReportConfiguration(boolean enabled,
                               int cooldown,
                               boolean showReporter, Sounds sound,
                               boolean closingReasonEnabled,
                               GuiItemConfig openReportsGui,
                               GuiItemConfig myReportsGui,
                               GuiItemConfig assignedReportsGui,
                               GuiItemConfig closedReportsGui,
                               String myReportsPermission, String myReportsCmd, boolean notifyReporterOnJoin,
                               List<ReportStatus> reporterNotifyStatuses,
                               List<ReportTypeConfiguration> reportTypeConfigurations,
                               boolean fixedReason, boolean fixedReasonCulprit, List<ReportReasonConfiguration> reportReasonConfigurations, List<ConfiguredCommand> acceptReportActions,
                               List<ConfiguredCommand> rejectReportActions,
                               List<ConfiguredCommand> reopenReportActions,
                               List<ConfiguredCommand> resolveReportActions) {
        this.enabled = enabled;
        this.cooldown = cooldown;
        this.showReporter = showReporter;
        this.sound = sound;
        this.closingReasonEnabled = closingReasonEnabled;
        this.openReportsGui = openReportsGui;
        this.myReportsGui = myReportsGui;
        this.assignedReportsGui = assignedReportsGui;
        this.closedReportsGui = closedReportsGui;
        this.myReportsPermission = myReportsPermission;
        this.myReportsCmd = myReportsCmd;
        this.notifyReporterOnJoin = notifyReporterOnJoin;
        this.reporterNotifyStatuses = reporterNotifyStatuses;
        this.reportTypeConfigurations = reportTypeConfigurations;
        this.fixedReason = fixedReason;
        this.fixedReasonCulprit = fixedReasonCulprit;
        this.reportReasonConfigurations = reportReasonConfigurations;
        this.acceptReportActions = acceptReportActions;
        this.rejectReportActions = rejectReportActions;
        this.reopenReportActions = reopenReportActions;
        this.resolveReportActions = resolveReportActions;
    }

    public boolean isFixedReason() {
        return fixedReason;
    }

    public boolean isFixedReasonCulprit() {
        return fixedReasonCulprit;
    }

    public boolean isClosingReasonEnabled() {
        return closingReasonEnabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getCooldown() {
        return cooldown;
    }

    public boolean isShowReporter() {
        return showReporter;
    }

    public Optional<Sounds> getSound() {
        return Optional.ofNullable(sound);
    }

    public GuiItemConfig getOpenReportsGui() {
        return openReportsGui;
    }

    public GuiItemConfig getMyAssignedReportsGui() {
        return myReportsGui;
    }

    public GuiItemConfig getClosedReportsGui() {
        return closedReportsGui;
    }

    public String getMyReportsPermission() {
        return myReportsPermission;
    }

    public String getMyReportsCmd() {
        return myReportsCmd;
    }

    public boolean isNotifyReporterOnJoin() {
        return notifyReporterOnJoin;
    }

    public List<ReportStatus> getReporterNotifyStatuses() {
        return reporterNotifyStatuses;
    }

    public GuiItemConfig getAssignedReportsGui() {
        return assignedReportsGui;
    }

    @SafeVarargs
    public final List<ReportTypeConfiguration> getReportTypeConfigurations(Predicate<Map<String, String>>... filterPredicates) {
        return reportTypeConfigurations.stream().filter(c ->c.filterMatches(filterPredicates)).collect(Collectors.toList());
    }

    @SafeVarargs
    public final List<ReportReasonConfiguration> getReportReasonConfigurations(Predicate<Map<String, String>>... filterPredicates) {
        return reportReasonConfigurations.stream().filter(c ->c.filterMatches(filterPredicates)).collect(Collectors.toList());
    }

    public List<ConfiguredCommand> getAcceptReportActions() {
        return acceptReportActions;
    }

    public List<ConfiguredCommand> getRejectReportActions() {
        return rejectReportActions;
    }

    public List<ConfiguredCommand> getReopenReportActions() {
        return reopenReportActions;
    }

    public List<ConfiguredCommand> getResolveReportActions() {
        return resolveReportActions;
    }
}
