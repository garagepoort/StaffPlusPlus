package net.shortninja.staffplus.core.domain.staff.reporting.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.reporting.Report;
import net.shortninja.staffplus.core.domain.staff.reporting.gui.actions.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class ManageReportGui extends AbstractGui {
    private static final int SIZE = 54;

    private final PermissionHandler permission = IocContainer.get(PermissionHandler.class);
    private final Options options = IocContainer.get(Options.class);
    private final ReportItemBuilder reportItemBuilder = IocContainer.get(ReportItemBuilder.class);
    private final Player player;
    private final Report report;

    public ManageReportGui(Player player, String title, Report report, Supplier<AbstractGui> previousGuiSupplier) {
        super(SIZE, title, previousGuiSupplier);
        this.player = player;
        this.report = report;
    }

    @Override
    public void buildGui() {

        IAction reopenAction = new ReopenReportAction();
        IAction resolveAction = new ResolveReportAction();
        IAction rejectAction = new RejectReportAction();
        IAction deleteAction = new DeleteReportAction();
        IAction teleportAction = new TeleportToReportLocationAction(report);

        setItem(13, reportItemBuilder.build(report), null);

        if(isAssignee() && permission.has(player, options.manageReportConfiguration.getPermissionResolve())) {
            addResolveItem(report, resolveAction, 34);
            addResolveItem(report, resolveAction, 35);
            addResolveItem(report, resolveAction, 43);
            addResolveItem(report, resolveAction, 44);
        }

        if(isAssignee() || permission.has(player, options.manageReportConfiguration.getPermissionReopenOther())) {
            addReopenItem(report, reopenAction, 27);
            addReopenItem(report, reopenAction, 28);
            addReopenItem(report, reopenAction, 36);
            addReopenItem(report, reopenAction, 37);
        }
        if(isAssignee() && permission.has(player, options.manageReportConfiguration.getPermissionReject())) {
            addRejectItem(report, rejectAction, 30);
            addRejectItem(report, rejectAction, 31);
            addRejectItem(report, rejectAction, 32);
            addRejectItem(report, rejectAction, 39);
            addRejectItem(report, rejectAction, 40);
            addRejectItem(report, rejectAction, 41);
        }
        if(isAssignee() && permission.has(player, options.manageReportConfiguration.getPermissionDelete())) {
            addDeleteItem(report, deleteAction, 8);
        }

        if(permission.has(player, options.manageReportConfiguration.getPermissionTeleport())) {
            addTeleportItem(teleportAction, 0);
        }
    }

    private boolean isAssignee() {
        return player.getUniqueId().equals(report.getStaffUuid());
    }

    private void addResolveItem(Report report, IAction action, int slot) {
        ItemStack item = StaffPlus.get().versionProtocol.addNbtString(
            Items.editor(Items.createGreenColoredGlass("Resolve report", "Click to mark this report as resolved"))
                .setAmount(1)
                .build(), String.valueOf(report.getId()));
        setItem(slot, item, action);
    }

    private void addRejectItem(Report report, IAction action, int slot) {
        ItemStack item = StaffPlus.get().versionProtocol.addNbtString(
            Items.editor(Items.createRedColoredGlass("Reject report", "Click to mark this report as rejected"))
                .setAmount(1)
                .build(), String.valueOf(report.getId()));
        setItem(slot, item, action);
    }

    private void addReopenItem(Report report, IAction action, int slot) {
        ItemStack item = StaffPlus.get().versionProtocol.addNbtString(
            Items.editor(Items.createWhiteColoredGlass("Unassign", "Click to unassign yourself from this report"))
                .setAmount(1)
                .build(), String.valueOf(report.getId()));
        setItem(slot, item, action);
    }

    private void addDeleteItem(Report report, IAction action, int slot) {
        ItemStack itemstack = Items.builder()
            .setMaterial(Material.REDSTONE_BLOCK)
            .setName("Delete")
            .addLore("Click to delete this report")
            .build();

        ItemStack item = StaffPlus.get().versionProtocol.addNbtString(
            Items.editor(itemstack)
                .setAmount(1)
                .build(), String.valueOf(report.getId()));
        setItem(slot, item, action);
    }

    private void addTeleportItem(IAction action, int slot) {
        ItemStack item = Items.createOrangeColoredGlass("Teleport", "Click to teleport to where this report was created");
        setItem(slot, item, action);
    }
}