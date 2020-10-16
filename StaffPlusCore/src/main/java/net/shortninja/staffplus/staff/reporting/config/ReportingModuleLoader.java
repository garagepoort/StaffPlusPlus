package net.shortninja.staffplus.staff.reporting.config;

import net.shortninja.staffplus.common.config.ConfigLoader;
import net.shortninja.staffplus.common.config.GuiItemConfig;
import net.shortninja.staffplus.event.ReportStatus;
import net.shortninja.staffplus.util.lib.Sounds;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class ReportingModuleLoader extends ConfigLoader<ReportConfiguration> {


    @Override
    public ReportConfiguration load() {
        boolean enabled = config.getBoolean("reports-module.enabled");
        int cooldown = config.getInt("reports-module.cooldown");
        boolean showReporter = config.getBoolean("reports-module.show-reporter");
        boolean notifyReportOnJoin = config.getBoolean("reports-module.reporter-notifications.notify-on-join");
        boolean closingReasonEnabled = config.getBoolean("reports-module.closing-reason-enabled", true);
        String deletionPermission = config.getString("permissions.report-deletion");
        Sounds sound = stringToSound(sanitize(config.getString("reports-module.sound", "NONE")));
        String myReportsPermission = config.getString("permissions.view-my-reports");
        String myReportsCmd = config.getString("commands.my-reports");
        List<ReportStatus> reporterNotifyStatuses = stream(config.getString("reports-module.reporter-notifications.status-change-notifications", "").split(";"))
            .filter(s -> !s.isEmpty())
            .map(ReportStatus::valueOf)
            .collect(Collectors.toList());

        boolean modeGuiReports = config.getBoolean("staff-mode.gui-module.reports-gui");
        String modeGuiReportsTitle = config.getString("staff-mode.gui-module.reports-title");
        String modeGuiReportsName = config.getString("staff-mode.gui-module.reports-name");
        String modeGuiReportsLore = config.getString("staff-mode.gui-module.reports-lore");
        String modeGuiMyReportsTitle = config.getString("staff-mode.gui-module.my-reports-title");
        String modeGuiMyReportsLore = config.getString("staff-mode.gui-module.my-reports-lore");
        String modeGuiClosedReportsTitle = config.getString("staff-mode.gui-module.closed-reports-title");
        String modeGuiClosedReportsLore = config.getString("staff-mode.gui-module.closed-reports-lore");

        GuiItemConfig openReportsGui = new GuiItemConfig(modeGuiReports, modeGuiReportsTitle, modeGuiReportsName, modeGuiReportsLore);
        GuiItemConfig myReportsGui = new GuiItemConfig(modeGuiReports, modeGuiMyReportsTitle, modeGuiMyReportsTitle, modeGuiMyReportsLore);
        GuiItemConfig closedReportsGui = new GuiItemConfig(modeGuiReports, modeGuiClosedReportsTitle, modeGuiClosedReportsTitle, modeGuiClosedReportsLore);

        return new ReportConfiguration(enabled, cooldown, showReporter, sound, closingReasonEnabled, openReportsGui, myReportsGui, closedReportsGui, myReportsPermission, myReportsCmd, notifyReportOnJoin, reporterNotifyStatuses, deletionPermission);
    }
}
