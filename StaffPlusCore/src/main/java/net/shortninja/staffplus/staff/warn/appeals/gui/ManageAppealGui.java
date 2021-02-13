package net.shortninja.staffplus.staff.warn.appeals.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.reporting.ManageReportService;
import net.shortninja.staffplus.staff.warn.appeals.Appeal;
import net.shortninja.staffplus.staff.warn.appeals.gui.actions.ApproveAppealAction;
import net.shortninja.staffplus.staff.warn.appeals.gui.actions.RejectAppealAction;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.util.Permission;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class ManageAppealGui extends AbstractGui {
    private static final int SIZE = 54;

    private final ManageReportService manageReportService = IocContainer.getManageReportService();
    private final Permission permission = IocContainer.getPermissionHandler();
    private final Options options = IocContainer.getOptions();
    private final Player player;
    private final Appeal appeal;

    public ManageAppealGui(Player player, String title, Appeal appeal, Supplier<AbstractGui> previousGuiSupplier) {
        super(SIZE, title, previousGuiSupplier);
        this.player = player;
        this.appeal = appeal;
    }

    @Override
    public void buildGui() {
        IAction approveAction = new ApproveAppealAction(appeal.getId());
        IAction rejectAction = new RejectAppealAction(appeal.getId());

        setItem(13, AppealItemBuilder.build(appeal), null);

        if (permission.has(player, options.appealConfiguration.getApproveAppealPermission())) {
            addApproveItem(approveAction, 34);
            addApproveItem(approveAction, 35);
            addApproveItem(approveAction, 43);
            addApproveItem(approveAction, 44);
        }


        if (permission.has(player, options.appealConfiguration.getRejectAppealPermission())) {
            addRejectItem(rejectAction, 30);
            addRejectItem(rejectAction, 31);
            addRejectItem(rejectAction, 32);
            addRejectItem(rejectAction, 39);
            addRejectItem(rejectAction, 40);
            addRejectItem(rejectAction, 41);
        }
    }

    private void addApproveItem(IAction action, int slot) {
        ItemStack item = Items.editor(Items.createGreenColoredGlass("Approve appeal", "Click to approve"))
            .setAmount(1)
            .build();
        setItem(slot, item, action);
    }

    private void addRejectItem(IAction action, int slot) {
        ItemStack item = Items.editor(Items.createRedColoredGlass("Reject appeal", "Click to reject"))
            .setAmount(1)
            .build();
        setItem(slot, item, action);
    }

}