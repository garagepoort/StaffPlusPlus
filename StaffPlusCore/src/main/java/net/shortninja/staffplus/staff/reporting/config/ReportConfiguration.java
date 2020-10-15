package net.shortninja.staffplus.staff.reporting.config;

import net.shortninja.staffplus.common.config.GuiItemConfig;
import net.shortninja.staffplus.event.ReportStatus;
import net.shortninja.staffplus.util.lib.Sounds;

import java.util.List;

public class ReportConfiguration {

    private final boolean enabled;
    private final int cooldown;
    private final boolean showReporter;
    private final Sounds sound;
    private final boolean closingReasonEnabled;
    private final GuiItemConfig openReportsGui;
    private final GuiItemConfig myReportsGui;
    private final GuiItemConfig closedReportsGui;
    private String myReportsPermission;
    private String myReportsCmd;
    private boolean notifyReporterOnJoin;
    private List<ReportStatus> reporterNotifyStatuses;


    public ReportConfiguration(boolean enabled,
                               int cooldown,
                               boolean showReporter, Sounds sound,
                               boolean closingReasonEnabled,
                               GuiItemConfig openReportsGui,
                               GuiItemConfig myReportsGui,
                               GuiItemConfig closedReportsGui,
                               String myReportsPermission, String myReportsCmd, boolean notifyReporterOnJoin,
                               List<ReportStatus> reporterNotifyStatuses) {
        this.enabled = enabled;
        this.cooldown = cooldown;
        this.showReporter = showReporter;
        this.sound = sound;
        this.closingReasonEnabled = closingReasonEnabled;
        this.openReportsGui = openReportsGui;
        this.myReportsGui = myReportsGui;
        this.closedReportsGui = closedReportsGui;
        this.myReportsPermission = myReportsPermission;
        this.myReportsCmd = myReportsCmd;
        this.notifyReporterOnJoin = notifyReporterOnJoin;
        this.reporterNotifyStatuses = reporterNotifyStatuses;
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

    public Sounds getSound() {
        return sound;
    }

    public GuiItemConfig getOpenReportsGui() {
        return openReportsGui;
    }

    public GuiItemConfig getMyReportsGui() {
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
}
