package net.shortninja.staffplus.staff.reporting.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.IAction;
import net.shortninja.staffplus.common.Items;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.reporting.ManageReportService;
import net.shortninja.staffplus.staff.reporting.Report;
import net.shortninja.staffplus.util.Permission;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ClosedReportManageGui extends AbstractGui {
    private static final int SIZE = 54;

    private final ManageReportService manageReportService = IocContainer.getManageReportService();
    private final Permission permission = IocContainer.getPermissionHandler();
    private final Options options = IocContainer.getOptions();
    private final Player player;
    private final Report report;

    public ClosedReportManageGui(Player player, String title, Report report) {
        super(SIZE, title);
        this.player = player;
        this.report = report;
    }

    @Override
    public void buildGui() {
        IAction deleteAction = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                int reportId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
                manageReportService.deleteReport(player, reportId);
            }

            @Override
            public boolean shouldClose(Player player) {
                return true;
            }
        };

        setItem(13, ReportItemBuilder.build(report), null);

        if (permission.has(player, options.manageReportConfiguration.getPermissionDelete())) {
            addDeleteItem(report, deleteAction, 31);
        }
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

}