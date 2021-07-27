package net.shortninja.staffplus.core.domain.staff.reporting.gui.views;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.InvestigationGuiComponent;
import net.shortninja.staffplus.core.domain.staff.reporting.Report;
import net.shortninja.staffplus.core.domain.staff.reporting.config.ManageReportConfiguration;
import net.shortninja.staffplusplus.reports.ReportStatus;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static be.garagepoort.mcioc.gui.TubingGuiActions.NOOP;

@IocBean
public class ReportDetailViewBuilder {
    private static final int SIZE = 54;

    private final PermissionHandler permission;
    private final ReportItemBuilder reportItemBuilder;
    private final ManageReportConfiguration manageReportConfiguration;
    private final InvestigationGuiComponent investigationGuiComponent;

    public ReportDetailViewBuilder(PermissionHandler permission, ReportItemBuilder reportItemBuilder, ManageReportConfiguration manageReportConfiguration, InvestigationGuiComponent investigationGuiComponent) {
        this.permission = permission;
        this.reportItemBuilder = reportItemBuilder;
        this.manageReportConfiguration = manageReportConfiguration;
        this.investigationGuiComponent = investigationGuiComponent;
    }

    public TubingGui buildGui(Player player, Report report, String backAction, String currentAction) {
        TubingGui.Builder builder = new TubingGui.Builder("Report by: " + report.getReporterName(), SIZE);
        builder.addItem(NOOP, 13, reportItemBuilder.build(report));

        if (report.getReportStatus() == ReportStatus.IN_PROGRESS) {
            if (isAssignee(player, report) && permission.has(player, manageReportConfiguration.permissionResolve)) {
                String resolveAction = "manage-reports/resolve?reportId=" + report.getId();
                builder.addItem(resolveAction, 34, getResolveItem());
                builder.addItem(resolveAction, 35, getResolveItem());
                builder.addItem(resolveAction, 43, getResolveItem());
                builder.addItem(resolveAction, 44, getResolveItem());
            }

            if (isAssignee(player, report) || permission.has(player, manageReportConfiguration.permissionReopenOther)) {
                String reopenAction = "manage-reports/reopen?reportId=" + report.getId();
                builder.addItem(reopenAction, 27, getReopenItem());
                builder.addItem(reopenAction, 28, getReopenItem());
                builder.addItem(reopenAction, 36, getReopenItem());
                builder.addItem(reopenAction, 37, getReopenItem());
            }
            if (isAssignee(player, report) && permission.has(player, manageReportConfiguration.permissionReject)) {
                String rejectAction = "manage-reports/reject?reportId=" + report.getId();
                builder.addItem(rejectAction, 30, getRejectItem());
                builder.addItem(rejectAction, 31, getRejectItem());
                builder.addItem(rejectAction, 32, getRejectItem());
                builder.addItem(rejectAction, 39, getRejectItem());
                builder.addItem(rejectAction, 40, getRejectItem());
                builder.addItem(rejectAction, 41, getRejectItem());
            }
        }

        if (isAssignee(player, report) && permission.has(player, manageReportConfiguration.permissionDelete)) {
            builder.addItem("manage-reports/delete?reportId=" + report.getId(), 8, getDeleteItem());
        }

        investigationGuiComponent.addEvidenceButton(builder, 14, report, currentAction);

        if (backAction != null) {
            builder.addItem(backAction, 49, Items.createDoor("Back", "Go back"));
        }

        if (permission.has(player, manageReportConfiguration.permissionTeleport)) {
            builder.addItem("manage-reports/teleport?reportId=" + report.getId(), 0, getTeleportItem());
        }
        return builder.build();
    }

    private boolean isAssignee(Player player, Report report) {
        return player.getUniqueId().equals(report.getStaffUuid()) && report.getReportStatus() != ReportStatus.OPEN;
    }

    private ItemStack getResolveItem() {
        return Items.createGreenColoredGlass("Resolve report", "Click to mark this report as resolved");
    }

    private ItemStack getRejectItem() {
        return Items.createRedColoredGlass("Reject report", "Click to mark this report as rejected");
    }

    private ItemStack getReopenItem() {
        return Items.createWhiteColoredGlass("Unassign", "Click to unassign yourself from this report");
    }

    private ItemStack getDeleteItem() {
        return Items.builder()
            .setMaterial(Material.REDSTONE_BLOCK)
            .setName("Delete")
            .addLore("Click to delete this report")
            .build();
    }

    private ItemStack getTeleportItem() {
        return Items.createOrangeColoredGlass("Teleport", "Click to teleport to where this report was created");
    }
}