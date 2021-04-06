package net.shortninja.staffplus.core.domain.staff.reporting.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.Sounds;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

import net.shortninja.staffplus.core.common.gui.GuiItemConfig;
import net.shortninja.staffplusplus.reports.ReportStatus;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@IocBean
public class ReportingModuleLoader extends AbstractConfigLoader<ReportConfiguration> {

    @Override
    protected ReportConfiguration load() {
        boolean enabled = defaultConfig.getBoolean("reports-module.enabled");
        int cooldown = defaultConfig.getInt("reports-module.cooldown");
        boolean showReporter = defaultConfig.getBoolean("reports-module.show-reporter");
        boolean notifyReportOnJoin = defaultConfig.getBoolean("reports-module.reporter-notifications.notify-on-join");
        boolean closingReasonEnabled = defaultConfig.getBoolean("reports-module.closing-reason-enabled", true);
        Sounds sound = stringToSound(sanitize(defaultConfig.getString("reports-module.sound", "NONE")));
        String myReportsPermission = defaultConfig.getString("permissions.view-my-reports");
        String myReportsCmd = defaultConfig.getString("commands.my-reports");
        List<ReportStatus> reporterNotifyStatuses = stream(defaultConfig.getString("reports-module.reporter-notifications.status-change-notifications", "").split(";"))
            .filter(s -> !s.isEmpty())
            .map(ReportStatus::valueOf)
            .collect(Collectors.toList());

        boolean modeGuiReports = staffModeModulesConfig.getBoolean("modules.gui-module.reports-gui");
        String modeGuiReportsTitle = staffModeModulesConfig.getString("modules.gui-module.reports-title");
        String modeGuiReportsName = staffModeModulesConfig.getString("modules.gui-module.reports-name");
        String modeGuiReportsLore = staffModeModulesConfig.getString("modules.gui-module.reports-lore");
        String modeGuiMyReportsTitle = staffModeModulesConfig.getString("modules.gui-module.my-reports-title");
        String modeGuiMyReportsLore = staffModeModulesConfig.getString("modules.gui-module.my-reports-lore");
        String modeGuiAssignedReportsTitle = staffModeModulesConfig.getString("modules.gui-module.assigned-reports-title");
        String modeGuiAssignedReportsLore = staffModeModulesConfig.getString("modules.gui-module.assigned-reports-lore");
        String modeGuiClosedReportsTitle = staffModeModulesConfig.getString("modules.gui-module.closed-reports-title");
        String modeGuiClosedReportsLore = staffModeModulesConfig.getString("modules.gui-module.closed-reports-lore");

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
            getReportTypes(defaultConfig));
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
