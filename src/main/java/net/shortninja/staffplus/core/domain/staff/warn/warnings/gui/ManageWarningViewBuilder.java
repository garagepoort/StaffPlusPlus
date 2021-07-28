package net.shortninja.staffplus.core.domain.staff.warn.warnings.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.TubingGui;
import be.garagepoort.mcioc.gui.TubingGuiActions;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.ExecutableActionEntity;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.InvestigationGuiComponent;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.gui.AppealItemBuilder;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.ManageWarningsConfiguration;
import net.shortninja.staffplusplus.warnings.AppealStatus;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

@IocBean
public class ManageWarningViewBuilder {
    private static final int SIZE = 54;

    private final ActionService actionService;
    private final PermissionHandler permission;
    private final Options options;
    private final WarningItemBuilder warningItemBuilder;
    private final InvestigationGuiComponent investigationGuiComponent;
    private final ManageWarningsConfiguration manageWarningsConfiguration;

    public ManageWarningViewBuilder(ActionService actionService, PermissionHandler permission, Options options, WarningItemBuilder warningItemBuilder, InvestigationGuiComponent investigationGuiComponent, ManageWarningsConfiguration manageWarningsConfiguration) {
        this.actionService = actionService;
        this.permission = permission;
        this.options = options;
        this.warningItemBuilder = warningItemBuilder;
        this.investigationGuiComponent = investigationGuiComponent;
        this.manageWarningsConfiguration = manageWarningsConfiguration;
    }

    public TubingGui buildGui(Player player, Warning warning, String currentAction, String backAction) {
        TubingGui.Builder builder = new TubingGui.Builder("Warning", 54);
//        IAction deleteAction = new DeleteWarningAction(warning, previousGuiSupplier);
        builder.addItem(TubingGuiActions.NOOP, 13, warningItemBuilder.build(warning));
        investigationGuiComponent.addEvidenceButton(builder, 14, warning, currentAction);

        if (permission.has(player, manageWarningsConfiguration.permissionDelete)) {
            addDeleteItem(builder, warning, backAction);
        }

        if (permission.has(player, manageWarningsConfiguration.permissionExpire) && !warning.isExpired() && !warning.hasApprovedAppeal()) {
            addExpireItem(builder, warning);
        }

        if (options.appealConfiguration.enabled) {
            if (!warning.getAppeal().isPresent() && canAddAppeal(player, warning)) {
                addCreateAppealItem(builder, warning);
            } else {
                addAppealInfoItem(player, builder, warning, currentAction);
            }
        }

        if (backAction != null) {
            builder.addItem(backAction, 49, Items.createDoor("Back", "Go back"));
        }

        return builder.build();
    }

    private boolean canAddAppeal(Player player, Warning warning) {
        if (permission.has(player, options.appealConfiguration.permissionCreateOthersAppeal)) {
            return true;
        }
        return permission.has(player, options.appealConfiguration.createAppealPermission) && warning.getTargetUuid().equals(player.getUniqueId());
    }

    private void addDeleteItem(TubingGui.Builder builder, Warning warning, String backAction) {
        List<String> rollbackCommands = actionService.getRollbackActions(warning).stream()
            .map(ExecutableActionEntity::getRollbackCommand)
            .collect(Collectors.toList());

        Items.ItemStackBuilder itemStackBuilder = Items.builder()
            .setMaterial(Material.REDSTONE_BLOCK)
            .setAmount(1)
            .setName("Delete")
            .addLore("Click to delete this warning");

        if (!rollbackCommands.isEmpty()) {
            itemStackBuilder.addLineLore();
            itemStackBuilder.addLore("&6Rollback actions:");
            for (String command : rollbackCommands) {
                itemStackBuilder.addLore("  - " + command);
            }
        }

        String deleteAction = GuiActionBuilder.builder()
            .action("manage-warnings/delete")
            .param("warningId", String.valueOf(warning.getId()))
            .param("backAction", backAction)
            .build();
        builder.addItem(deleteAction, 8, itemStackBuilder.build());
    }

    private void addExpireItem(TubingGui.Builder builder, Warning warning) {
        ItemStack item = Items.createOrangeColoredGlass("Expire", "Click to expire this warning");
        builder.addItem("manage-warnings/expire?warningId=" + warning.getId(), 26, item);
    }

    private void addCreateAppealItem(TubingGui.Builder builder, Warning warning) {
        String appealAction = options.appealConfiguration.fixedAppealReason ?
            "manage-warning-appeals/view/create/reason-select" :
            "manage-warning-appeals/view/create/reason-chat";

        ItemStack item = Items.builder()
            .setMaterial(Material.BOOK)
            .setName("Add Appeal")
            .addLore("Click to appeal this warning")
            .build();

        builder.addItem(appealAction + "?warningId=" + warning.getId(), 31, item);
    }

    private void addAppealInfoItem(Player player, TubingGui.Builder builder, Warning warning, String backAction) {
        ItemStack item = AppealItemBuilder.build(warning.getAppeal().get());

        String action = TubingGuiActions.NOOP;
        if (canManageAppeals(player) && warning.getAppeal().get().getStatus() == AppealStatus.OPEN) {
            action = GuiActionBuilder.builder()
                .action("manage-warning-appeals/view/detail")
                .param("appealId", String.valueOf(warning.getAppeal().get().getId()))
                .param("backAction", backAction)
                .build();
        }

        builder.addItem(action, 31, item);
    }

    private boolean canManageAppeals(Player player) {
        return permission.has(player, options.appealConfiguration.approveAppealPermission) || permission.has(player, options.appealConfiguration.rejectAppealPermission);
    }
}