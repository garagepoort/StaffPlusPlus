package net.shortninja.staffplus.core.domain.staff.warn.appeals.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.TubingGui;
import be.garagepoort.mcioc.gui.TubingGuiActions;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.ExecutableActionEntity;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.Appeal;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

@IocBean
public class ManageAppealViewBuilder {
    private static final int SIZE = 54;

    private final PermissionHandler permission;
    private final ActionService actionService;
    private final Options options;

    public ManageAppealViewBuilder(PermissionHandler permission, ActionService actionService, Options options) {
        this.permission = permission;
        this.actionService = actionService;
        this.options = options;
    }

    public TubingGui buildGui(Player player, Appeal appeal, Warning warning, String backAction) {
        TubingGui.Builder builder = new TubingGui.Builder("Manage appeal", SIZE);
        builder.addItem(TubingGuiActions.NOOP, 13, AppealItemBuilder.build(appeal));


        if (permission.has(player, options.appealConfiguration.approveAppealPermission)) {
            List<String> actions = actionService.getRollbackActions(warning).stream()
                .map(ExecutableActionEntity::getRollbackCommand)
                .collect(Collectors.toList());

            addApproveItem(appeal, builder, 34, actions);
            addApproveItem(appeal, builder, 35, actions);
            addApproveItem(appeal, builder, 43, actions);
            addApproveItem(appeal, builder, 44, actions);
        }


        if (permission.has(player, options.appealConfiguration.rejectAppealPermission)) {
            addRejectItem(appeal, builder, 30);
            addRejectItem(appeal, builder, 31);
            addRejectItem(appeal, builder, 32);
            addRejectItem(appeal, builder, 39);
            addRejectItem(appeal, builder, 40);
            addRejectItem(appeal, builder, 41);
        }

        if (backAction != null) {
            builder.addItem(backAction, 49, Items.createDoor("Back", "Go back"));
        }

        return builder.build();
    }

    private void addApproveItem(Appeal appeal, TubingGui.Builder builder, int slot, List<String> rollbackCommands) {
        Items.ItemStackBuilder itemStackBuilder = Items.editor(Items.createGreenColoredGlass("Approve appeal", "Click to approve"))
            .setAmount(1);

        if (!rollbackCommands.isEmpty()) {
            itemStackBuilder.addLineLore();
            itemStackBuilder.addLore("&6Rollback actions:");
            for (String command : rollbackCommands) {
                itemStackBuilder.addLore("  - " + command);
            }
        }

        ItemStack item = itemStackBuilder.build();
        String action = GuiActionBuilder.builder()
            .action("manage-warning-appeals/approve")
            .param("appealId", String.valueOf(appeal.getId()))
            .build();
        builder.addItem("manage-warning-appeals/approve", slot, item);
    }

    private void addRejectItem(Appeal appeal, TubingGui.Builder builder, int slot) {
        ItemStack item = Items.editor(Items.createRedColoredGlass("Reject appeal", "Click to reject"))
            .setAmount(1)
            .build();
        String action = GuiActionBuilder.builder()
            .action("manage-warning-appeals/reject")
            .param("appealId", String.valueOf(appeal.getId()))
            .build();
        builder.addItem(action, slot, item);
    }

}