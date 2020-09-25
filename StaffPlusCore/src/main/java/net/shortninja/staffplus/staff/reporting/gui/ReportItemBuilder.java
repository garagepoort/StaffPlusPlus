package net.shortninja.staffplus.staff.reporting.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.staff.reporting.Report;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.inventory.ItemStack;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReportItemBuilder {

    public static ItemStack build(Report report) {
        List<String> lore = new ArrayList<String>();

        lore.add("&bStatus: " + report.getReportStatus());
        lore.add("&bTimeStamp: " + report.getTimestamp().format(DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm")));
        if (IocContainer.getOptions().reportConfiguration.isShowReporter()) {
            lore.add("&bReporter: " + report.getReporterName());
        }

        lore.add("&bReason: " + report.getReason());

        String culprit = report.getCulpritName() == null ? "Unknown" : report.getCulpritName();
        ItemStack item = Items.editor(Items.createSkull(report.getCulpritName())).setAmount(1)
                .setName("&bCulprit: " + culprit)
                .setLore(lore)
                .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, String.valueOf(report.getId()));
    }
}
