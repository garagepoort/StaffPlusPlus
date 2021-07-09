package net.shortninja.staffplus.core.domain.staff.warn.appeals.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.ExecutableActionEntity;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.Appeal;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.gui.actions.ApproveAppealAction;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.gui.actions.RejectAppealAction;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ManageAppealGui extends AbstractGui {
    private static final int SIZE = 54;

    private final PermissionHandler permission = StaffPlus.get().getIocContainer().get(PermissionHandler.class);
    private final ActionService actionService = StaffPlus.get().getIocContainer().get(ActionService.class);
    private final Options options = StaffPlus.get().getIocContainer().get(Options.class);

    private final Player player;
    private final Warning warning;
    private final Appeal appeal;

    public ManageAppealGui(Player player, String title, Warning warning, Appeal appeal, Supplier<AbstractGui> previousGuiSupplier) {
        super(SIZE, title, previousGuiSupplier);
        this.player = player;
        this.warning = warning;
        this.appeal = appeal;
    }

    @Override
    public void buildGui() {
        IAction approveAction = new ApproveAppealAction(appeal.getId());
        IAction rejectAction = new RejectAppealAction(appeal.getId());

        setItem(13, AppealItemBuilder.build(appeal), null);


        if (permission.has(player, options.appealConfiguration.approveAppealPermission)) {
            List<String> actions = actionService.getRollbackActions(warning).stream()
                .map(ExecutableActionEntity::getRollbackCommand)
                .collect(Collectors.toList());

            addApproveItem(approveAction, 34, actions);
            addApproveItem(approveAction, 35, actions);
            addApproveItem(approveAction, 43, actions);
            addApproveItem(approveAction, 44, actions);
        }


        if (permission.has(player, options.appealConfiguration.rejectAppealPermission)) {
            addRejectItem(rejectAction, 30);
            addRejectItem(rejectAction, 31);
            addRejectItem(rejectAction, 32);
            addRejectItem(rejectAction, 39);
            addRejectItem(rejectAction, 40);
            addRejectItem(rejectAction, 41);
        }
    }

    private void addApproveItem(IAction action, int slot, List<String> rollbackCommands) {
        Items.ItemStackBuilder itemStackBuilder = Items.editor(Items.createGreenColoredGlass("Approve appeal", "Click to approve"))
            .setAmount(1);

        if(!rollbackCommands.isEmpty()) {
            itemStackBuilder.addLineLore();
            itemStackBuilder.addLore("&6Rollback actions:");
            for (String command : rollbackCommands) {
                itemStackBuilder.addLore("  - " + command);
            }
        }

        ItemStack item = itemStackBuilder.build();
        setItem(slot, item, action);
    }

    private void addRejectItem(IAction action, int slot) {
        ItemStack item = Items.editor(Items.createRedColoredGlass("Reject appeal", "Click to reject"))
            .setAmount(1)
            .build();
        setItem(slot, item, action);
    }

}