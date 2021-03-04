package net.shortninja.staffplus.staff.reporting.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.cmd.CommandUtil;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.reporting.ManageReportService;
import net.shortninja.staffplus.staff.reporting.Report;
import net.shortninja.staffplus.common.IAction;
import net.shortninja.staffplus.util.Permission;
import net.shortninja.staffplus.common.Items;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class ManageReportGui extends AbstractGui {
    private static final int SIZE = 54;

    private final ManageReportService manageReportService = IocContainer.getManageReportService();
    private final Permission permission = IocContainer.getPermissionHandler();
    private final Options options = IocContainer.getOptions();
    private final Player player;
    private final Report report;

    public ManageReportGui(Player player, String title, Report report, Supplier<AbstractGui> previousGuiSupplier) {
        super(SIZE, title, previousGuiSupplier);
        this.player = player;
        this.report = report;
    }

    @Override
    public void buildGui() {


        IAction reopenAction = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                CommandUtil.playerAction(player, () -> {
                    int reportId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
                    manageReportService.reopenReport(player, reportId);
                });
            }

            @Override
            public boolean shouldClose(Player player) {
                return true;
            }
        };

        IAction resolveAction = new ResolveReportAction();
        IAction rejectAction = new RejectReportAction();
        IAction deleteAction = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                CommandUtil.playerAction(player, () -> {
                    int reportId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
                    manageReportService.deleteReport(player, reportId);
                });
            }

            @Override
            public boolean shouldClose(Player player) {
                return true;
            }
        };
        IAction teleportAction = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                CommandUtil.playerAction(player, () -> {
                    if(report.getLocation().isPresent()) {
                        Location location = report.getLocation().get();
                        player.teleport(location);
                        message.send(player, "You have been teleported to the location where this report was created", messages.prefixReports);
                    }else {
                        message.send(player, "&cLocation not known for this report.", messages.prefixReports);
                    }
                });
            }

            @Override
            public boolean shouldClose(Player player) {
                return true;
            }
        };

        setItem(13, ReportItemBuilder.build(report), null);

        if(permission.has(player, options.manageReportConfiguration.getPermissionResolve())) {
            addResolveItem(report, resolveAction, 34);
            addResolveItem(report, resolveAction, 35);
            addResolveItem(report, resolveAction, 43);
            addResolveItem(report, resolveAction, 44);
        }

        addReopenItem(report, reopenAction, 27);
        addReopenItem(report, reopenAction, 28);
        addReopenItem(report, reopenAction, 36);
        addReopenItem(report, reopenAction, 37);

        if(permission.has(player, options.manageReportConfiguration.getPermissionReject())) {
            addRejectItem(report, rejectAction, 30);
            addRejectItem(report, rejectAction, 31);
            addRejectItem(report, rejectAction, 32);
            addRejectItem(report, rejectAction, 39);
            addRejectItem(report, rejectAction, 40);
            addRejectItem(report, rejectAction, 41);
        }
        if(permission.has(player, options.manageReportConfiguration.getPermissionDelete())) {
            addDeleteItem(report, deleteAction, 8);
        }
        if(permission.has(player, options.manageReportConfiguration.getPermissionTeleport())) {
            addTeleportItem(teleportAction, 0);
        }
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