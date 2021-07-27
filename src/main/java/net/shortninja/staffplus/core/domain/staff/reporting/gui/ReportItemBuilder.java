package net.shortninja.staffplus.core.domain.staff.reporting.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionType;
import net.shortninja.staffplus.core.domain.staff.infractions.gui.views.InfractionGuiProvider;
import net.shortninja.staffplus.core.domain.staff.reporting.Report;
import net.shortninja.staffplusplus.reports.ReportStatus;
import org.apache.commons.lang.StringUtils;
import org.bukkit.inventory.ItemStack;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static net.shortninja.staffplus.core.common.JavaUtils.formatLines;

@IocBean
@IocMultiProvider(InfractionGuiProvider.class)
public class ReportItemBuilder implements InfractionGuiProvider<Report> {

    private static final String TAG_COLOR = "&b";
    private static final String VALUE_COLOR = "&7";
    private static final String UNKNOWN = "Unknown";

    private final IProtocolService protocolService;
    private final Options options;

    public ReportItemBuilder(IProtocolService protocolService, Options options) {
        this.protocolService = protocolService;
        this.options = options;
    }

    public ItemStack build(Report report) {
        List<String> lore = new ArrayList<>();

        addLoreLine(lore, "Id", String.valueOf(report.getId()));
        if (options.serverSyncConfiguration.isReportSyncEnabled()) {
            addLoreLine(lore, "Server", report.getServerName());
        }
        report.getReportType().ifPresent(type -> addLoreLine(lore, "Type", type));
        if (report.getReportStatus() != ReportStatus.OPEN) {
            addLoreLine(lore, "Assignee", report.getStaffName());
        }

        addLoreLine(lore, "Culprit", report.getCulpritName() == null ? UNKNOWN : report.getCulpritName());
        addLoreLine(lore, "Status", report.getReportStatus().name());
        addLoreLine(lore, "TimeStamp: ", report.getCreationDate().format(DateTimeFormatter.ofPattern(options.timestampFormat)));
        if (options.reportConfiguration.isShowReporter()) {
            addLoreLine(lore, "Reporter", report.getReporterName());
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

        if (report.getSppLocation().isPresent()) {
            addLoreLine(lore, "Location", report.getSppLocation().get().getWorldName() + " &8 | &7" + JavaUtils.serializeLocation(report.getSppLocation().get()));
        } else {
            addLoreLine(lore, "Location", UNKNOWN);
        }

        ItemStack item = Items.editor(Items.createSkull(report.getCulpritName())).setAmount(1)
            .setName(TAG_COLOR + "Culprit: " + (report.getCulpritName() == null ? UNKNOWN : report.getCulpritName()))
            .setName("&5Report")
            .setLore(lore)
            .build();

        return protocolService.getVersionProtocol().addNbtString(item, String.valueOf(report.getId()));
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
        itemStack.setType(options.infractionsConfiguration.getReportedGuiItem());
        return itemStack;
    }
}
