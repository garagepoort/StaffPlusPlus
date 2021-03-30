package net.shortninja.staffplus.core.domain.staff.reporting.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.reporting.ManageReportService;
import net.shortninja.staffplus.core.domain.staff.reporting.Report;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ClosedReportManageGui extends AbstractGui {
    private static final int SIZE = 54;

    private final ManageReportService manageReportService = StaffPlus.get().getIocContainer().get(ManageReportService.class);
    private final PermissionHandler permission = StaffPlus.get().getIocContainer().get(PermissionHandler.class);
    private final Options options = StaffPlus.get().getIocContainer().get(Options.class);
    private final ReportItemBuilder reportItemBuilder = StaffPlus.get().getIocContainer().get(ReportItemBuilder.class);
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
                int reportId = Integer.parseInt(StaffPlus.get().getIocContainer().get(IProtocolService.class).getVersionProtocol().getNbtString(item));
                manageReportService.deleteReport(player, reportId);
            }

            @Override
            public boolean shouldClose(Player player) {
                return true;
            }
        };

        setItem(13, reportItemBuilder.build(report), null);

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

        ItemStack item = StaffPlus.get().getIocContainer().get(IProtocolService.class).getVersionProtocol().addNbtString(
            Items.editor(itemstack)
                .setAmount(1)
                .build(), String.valueOf(report.getId()));
        setItem(slot, item, action);
    }

}