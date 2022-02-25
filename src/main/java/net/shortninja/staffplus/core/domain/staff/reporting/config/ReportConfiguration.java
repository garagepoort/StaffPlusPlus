package net.shortninja.staffplus.core.domain.staff.reporting.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import net.shortninja.staffplus.core.application.config.SoundsConfigTransformer;
import net.shortninja.staffplus.core.common.Sounds;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommandsConfigTransformer;
import net.shortninja.staffplusplus.reports.ReportStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@IocBean
public class ReportConfiguration {

    @ConfigProperty("reports-module.enabled")
    private boolean enabled;
    @ConfigProperty("reports-module.cooldown")
    private int cooldown;
    @ConfigProperty("reports-module.show-reporter")
    private boolean showReporter;
    @ConfigProperty("reports-module.sound")
    @ConfigTransformer(SoundsConfigTransformer.class)
    private Sounds sound;
    @ConfigProperty("reports-module.closing-reason-enabled")
    private boolean closingReasonEnabled;

    @ConfigProperty("permissions:view-my-reports")
    private String myReportsPermission;
    @ConfigProperty("commands:my-reports")
    private List<String> myReportsCmd;
    @ConfigProperty("reports-module.reporter-notifications.notify-on-join")
    private boolean notifyReporterOnJoin;

    @ConfigProperty("reports-module.reporter-notifications.status-change-notifications")
    @ConfigTransformer(ReportStatusesConfigTransformer.class)
    private List<ReportStatus> reporterNotifyStatuses;

    @ConfigProperty("reports-module.report-types")
    @ConfigTransformer(ReportTypeConfigTransformer.class)
    private List<ReportTypeConfiguration> reportTypeConfigurations;

    @ConfigProperty("reports-module.reasons")
    @ConfigTransformer(ReportReasonConfigTransformer.class)
    private List<ReportReasonConfiguration> reportReasonConfigurations;
    @ConfigProperty("reports-module.fixed-reason")
    private boolean fixedReason;
    @ConfigProperty("reports-module.fixed-reason-culprit")
    private boolean fixedReasonCulprit;

    @ConfigProperty("reports-module.accept-commands")
    @ConfigTransformer(ConfiguredCommandsConfigTransformer.class)
    private List<ConfiguredCommand> acceptReportActions;

    @ConfigProperty("reports-module.reject-commands")
    @ConfigTransformer(ConfiguredCommandsConfigTransformer.class)
    private List<ConfiguredCommand> rejectReportActions;

    @ConfigProperty("reports-module.reopen-commands")
    @ConfigTransformer(ConfiguredCommandsConfigTransformer.class)
    private List<ConfiguredCommand> reopenReportActions;

    @ConfigProperty("reports-module.resolve-commands")
    @ConfigTransformer(ConfiguredCommandsConfigTransformer.class)
    private List<ConfiguredCommand> resolveReportActions;

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

    public String getMyReportsPermission() {
        return myReportsPermission;
    }

    public String getMyReportsCmd() {
        return myReportsCmd.get(0);
    }

    public boolean isNotifyReporterOnJoin() {
        return notifyReporterOnJoin;
    }

    public List<ReportStatus> getReporterNotifyStatuses() {
        return reporterNotifyStatuses;
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
