package net.shortninja.staffplus.core.domain.staff.reporting.config;

import net.shortninja.staffplus.core.common.Sounds;
import net.shortninja.staffplus.core.common.config.ConfigLoader;
import net.shortninja.staffplus.core.common.gui.GuiItemConfig;
import net.shortninja.staffplusplus.reports.ReportStatus;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class ReportingModuleLoader extends ConfigLoader<ReportConfiguration> {

    @Override
    protected ReportConfiguration load(FileConfiguration config) {
        boolean enabled = config.getBoolean("reports-module.enabled");
        int cooldown = config.getInt("reports-module.cooldown");
        boolean showReporter = config.getBoolean("reports-module.show-reporter");
        boolean notifyReportOnJoin = config.getBoolean("reports-module.reporter-notifications.notify-on-join");
        boolean closingReasonEnabled = config.getBoolean("reports-module.closing-reason-enabled", true);
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
        String modeGuiAssignedReportsTitle = config.getString("staff-mode.gui-module.assigned-reports-title");
        String modeGuiAssignedReportsLore = config.getString("staff-mode.gui-module.assigned-reports-lore");
        String modeGuiClosedReportsTitle = config.getString("staff-mode.gui-module.closed-reports-title");
        String modeGuiClosedReportsLore = config.getString("staff-mode.gui-module.closed-reports-lore");

        GuiItemConfig openReportsGui = new GuiItemConfig(modeGuiReports, modeGuiReportsTitle, modeGuiReportsName, modeGuiReportsLore);
        GuiItemConfig myReportsGui = new GuiItemConfig(modeGuiReports, modeGuiMyReportsTitle, modeGuiMyReportsTitle, modeGuiMyReportsLore);
        GuiItemConfig assignedReportsGui = new GuiItemConfig(modeGuiReports, modeGuiAssignedReportsTitle, modeGuiAssignedReportsTitle, modeGuiAssignedReportsLore);
        GuiItemConfig closedReportsGui = new GuiItemConfig(modeGuiReports, modeGuiClosedReportsTitle, modeGuiClosedReportsTitle, modeGuiClosedReportsLore);

        return new ReportConfiguration(enabled,
            cooldown,
            showReporter,
            sound,
            closingReasonEnabled,
            openReportsGui,
            myReportsGui,
            assignedReportsGui,
            closedReportsGui,
            myReportsPermission,
            myReportsCmd,
            notifyReportOnJoin,
            reporterNotifyStatuses,
            getReportTypes(config));
    }

    private List<ReportTypeConfiguration> getReportTypes(FileConfiguration config) {
        List<LinkedHashMap<String, String>> list = (List<LinkedHashMap<String, String>>) config.getList("reports-module.report-types", new ArrayList<>());

        return Objects.requireNonNull(list).stream().map(map -> {
            String name = map.get("name");
            String lore = map.get("info");
            Material material = map.containsKey("material") ? Material.valueOf(map.get("material")) : Material.PAPER;
            return new ReportTypeConfiguration(name, material, lore);
        }).collect(Collectors.toList());
    }
}
