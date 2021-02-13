package net.shortninja.staffplus.staff.warn.appeals.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.actions.ActionService;
import net.shortninja.staffplus.common.actions.ExecutableActionEntity;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.warn.appeals.Appeal;
import net.shortninja.staffplus.staff.warn.appeals.gui.actions.ApproveAppealAction;
import net.shortninja.staffplus.staff.warn.appeals.gui.actions.RejectAppealAction;
import net.shortninja.staffplus.staff.warn.warnings.Warning;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.util.Permission;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ManageAppealGui extends AbstractGui {
    private static final int SIZE = 54;

    private final Permission permission = IocContainer.getPermissionHandler();
    private final ActionService actionService = IocContainer.getActionService();
    private final Options options = IocContainer.getOptions();

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


        if (permission.has(player, options.appealConfiguration.getApproveAppealPermission())) {
            List<String> actions = actionService.getRollbackActions(warning).stream()
                .map(ExecutableActionEntity::getRollbackCommand)
                .collect(Collectors.toList());

            addApproveItem(approveAction, 34, actions);
            addApproveItem(approveAction, 35, actions);
            addApproveItem(approveAction, 43, actions);
            addApproveItem(approveAction, 44, actions);
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