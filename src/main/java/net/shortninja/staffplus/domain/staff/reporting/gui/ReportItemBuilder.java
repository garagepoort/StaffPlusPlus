package net.shortninja.staffplus.domain.staff.reporting.gui;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.domain.staff.infractions.InfractionType;
import net.shortninja.staffplus.domain.staff.infractions.gui.InfractionGuiProvider;
import net.shortninja.staffplus.domain.staff.reporting.Report;
import net.shortninja.staffplus.common.JavaUtils;
import net.shortninja.staffplus.common.Items;
import net.shortninja.staffplusplus.reports.ReportStatus;
import org.apache.commons.lang.StringUtils;
import org.bukkit.inventory.ItemStack;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static net.shortninja.staffplus.common.JavaUtils.formatLines;

public class ReportItemBuilder implements InfractionGuiProvider<Report> {

    private static final String TAG_COLOR = "&b";
    private static final String VALUE_COLOR = "&7";
    private static final String UNKNOWN = "Unknown";

    public static ItemStack build(Report report) {
        List<String> lore = new ArrayList<>();

        addLoreLine(lore, "Id", String.valueOf(report.getId()));
        if(IocContainer.getOptions().serverSyncConfiguration.isReportSyncEnabled()) {
            addLoreLine(lore,"Server", report.getServerName());
        }
        report.getReportType().ifPresent(type -> addLoreLine(lore,"Type", type));
        if(report.getReportStatus() != ReportStatus.OPEN) {
            addLoreLine(lore,"Assignee", report.getStaffName());
        }

        addLoreLine(lore,"Culprit", report.getCulpritName() == null ? UNKNOWN : report.getCulpritName());
        addLoreLine(lore,"Status", report.getReportStatus().name());
        addLoreLine(lore,"TimeStamp: ", report.getCreationDate().format(DateTimeFormatter.ofPattern(IocContainer.getOptions().timestampFormat)));
        if (IocContainer.getOptions().reportConfiguration.isShowReporter()) {
            addLoreLine(lore,"Reporter", report.getReporterName());
        }

        lore.add(TAG_COLOR + "Reason:");
        for (String line : formatLines(report.getReason(), 30)) {
            lore.add("  " + VALUE_COLOR + line);
        }

        if (StringUtils.isNotEmpty(report.getCloseReason())) {
            lore.add(TAG_COLOR + "Close reason:");
            for (String line : formatLines(report.getCloseReason(), 30)) {
                lore.add("  " + VALUE_COLOR + line);
            }
        }

        if(report.getSppLocation().isPresent()) {
            addLoreLine(lore,"Location", report.getSppLocation().get().getWorldName() + " &8 | &7" + JavaUtils.serializeLocation(report.getSppLocation().get()));
        }else {
            addLoreLine(lore,"Location", UNKNOWN);
        }

        ItemStack item = Items.editor(Items.createSkull(report.getCulpritName())).setAmount(1)
            .setName(TAG_COLOR + "Culprit: " + (report.getCulpritName() == null ? UNKNOWN : report.getCulpritName()))
            .setName("&5Report")
            .setLore(lore)
            .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, String.valueOf(report.getId()));
    }

    private static void addLoreLine(List<String> lore, String tag, String value) {
        lore.add(TAG_COLOR + tag + ": " + VALUE_COLOR + value);
    }


    @Override
    public InfractionType getType() {
        return InfractionType.REPORTED;
    }

    @Override
    public ItemStack getMenuItem(Report report) {
        ItemStack itemStack = build(report);
        itemStack.setType(IocContainer.getOptions().infractionsConfiguration.getReportedGuiItem());
        return itemStack;
    }
}
