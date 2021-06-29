package net.shortninja.staffplus.core.domain.staff.warn.warnings.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.ExecutableActionEntity;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.InvestigationGuiComponent;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.gui.AppealItemBuilder;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.gui.AppealReasonChatAction;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.gui.AppealReasonSelectAction;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.gui.actions.GoToManageAppealGuiAction;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.gui.actions.DeleteWarningAction;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.gui.actions.ExpireWarningAction;
import net.shortninja.staffplusplus.warnings.AppealStatus;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ManageWarningGui extends AbstractGui {
    private static final int SIZE = 54;

    private final ActionService actionService = StaffPlus.get().getIocContainer().get(ActionService.class);
    private final PermissionHandler permission = StaffPlus.get().getIocContainer().get(PermissionHandler.class);
    private final Options options = StaffPlus.get().getIocContainer().get(Options.class);
    private final WarnService warnService = StaffPlus.get().getIocContainer().get(WarnService.class);
    private final WarningItemBuilder warningItemBuilder = StaffPlus.get().getIocContainer().get(WarningItemBuilder.class);
    private final InvestigationGuiComponent investigationGuiComponent = StaffPlus.get().getIocContainer().get(InvestigationGuiComponent.class);

    private final Player player;
    private final Warning warning;
    private final Supplier<AbstractGui> goBack;

    public ManageWarningGui(Player player, String title, Warning warning, Supplier<AbstractGui> previousGuiSupplier) {
        super(SIZE, title, previousGuiSupplier);
        this.player = player;
        this.warning = warning;
        goBack = () -> new ManageWarningGui(player, title, warnService.getWarning(warning.getId()), previousGuiSupplier);
    }

    @Override
    public void buildGui() {
        IAction deleteAction = new DeleteWarningAction(warning, previousGuiSupplier);
        setItem(13, warningItemBuilder.build(warning), null);
        investigationGuiComponent.addEvidenceButton(this, 14, warning);

        if (permission.has(player, options.manageWarningsConfiguration.getPermissionDelete())) {
            addDeleteItem(warning, deleteAction, 8);
        }

        if (permission.has(player, options.manageWarningsConfiguration.getPermissionExpire()) && !warning.isExpired() && !warning.hasApprovedAppeal()) {
            addExpireItem(warning, 26);
        }

        if (options.appealConfiguration.isEnabled()) {
            if (!warning.getAppeal().isPresent()) {
                if (canAddAppeal(player, warning)) {
                    addAppealItem(31);
                }
            } else {
                addAppealInfoItem(31);
            }
        }
    }

    private boolean canAddAppeal(Player player, Warning warning) {
        if (permission.has(player, options.appealConfiguration.getCreateOthersAppealPermission())) {
            return true;
        }
        return permission.has(player, options.appealConfiguration.getCreateAppealPermission()) && warning.getTargetUuid().equals(player.getUniqueId());
    }

    private void addDeleteItem(Warning warning, IAction action, int slot) {
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

        setItem(slot, itemStackBuilder.build(), action);
    }

    private void addExpireItem(Warning warning, int slot) {
        ItemStack item = Items.createOrangeColoredGlass("Expire","Click to expire this warning");
        setItem(slot, item, new ExpireWarningAction(warning));
    }

    private void addAppealItem(int slot) {
        IAction appealAction = null;
        if (!warning.getAppeal().isPresent()) {
            appealAction = options.appealConfiguration.isFixedAppealReason() ?
                new AppealReasonSelectAction(warning, goBack) :
                new AppealReasonChatAction(warning);
        }

        ItemStack item = Items.builder()
            .setMaterial(Material.BOOK)
            .setName("Add Appeal")
            .addLore("Click to appeal this warning")
            .build();
        setItem(slot, item, appealAction);
    }

    private void addAppealInfoItem(int slot) {
        ItemStack item = AppealItemBuilder.build(warning.getAppeal().get());

        IAction action = null;
        if (canManageAppeals() && warning.getAppeal().get().getStatus() == AppealStatus.OPEN) {
            action = new GoToManageAppealGuiAction(warning, warning.getAppeal().get(), goBack);
        }

        setItem(slot, item, action);
    }

    private boolean canManageAppeals() {
        return permission.has(player, options.appealConfiguration.getApproveAppealPermission()) || permission.has(player, options.appealConfiguration.getRejectAppealPermission());
    }
}