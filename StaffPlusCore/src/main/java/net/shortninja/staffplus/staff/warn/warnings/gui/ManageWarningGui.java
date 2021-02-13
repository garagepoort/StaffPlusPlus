package net.shortninja.staffplus.staff.warn.warnings.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.actions.ActionService;
import net.shortninja.staffplus.common.actions.ExecutableActionEntity;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.warn.appeals.gui.AppealItemBuilder;
import net.shortninja.staffplus.staff.warn.appeals.gui.AppealReasonChatAction;
import net.shortninja.staffplus.staff.warn.appeals.gui.AppealReasonSelectAction;
import net.shortninja.staffplus.staff.warn.appeals.gui.actions.GoToManageAppealGuiAction;
import net.shortninja.staffplus.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.staff.warn.warnings.Warning;
import net.shortninja.staffplus.staff.warn.warnings.gui.actions.DeleteWarningAction;
import net.shortninja.staffplus.unordered.AppealStatus;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.util.Permission;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ManageWarningGui extends AbstractGui {
    private static final int SIZE = 54;

    private final ActionService actionService = IocContainer.getActionService();
    private final Permission permission = IocContainer.getPermissionHandler();
    private final Options options = IocContainer.getOptions();
    private final WarnService warnService = IocContainer.getWarnService();

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
        IAction deleteAction = new DeleteWarningAction();
        setItem(13, WarningItemBuilder.build(warning), null);

        if (permission.has(player, options.manageWarningsConfiguration.getPermissionDelete())) {
            addDeleteItem(warning, deleteAction, 8);
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
        return permission.has(player, options.appealConfiguration.getCreateAppealPermission()) && warning.getUuid().equals(player.getUniqueId());
    }

    private void addDeleteItem(Warning warning, IAction action, int slot) {
        List<String> rollbackCommands = actionService.getRollbackActions(warning).stream()
            .map(ExecutableActionEntity::getRollbackCommand)
            .collect(Collectors.toList());

        Items.ItemStackBuilder itemStackBuilder = Items.builder()
            .setMaterial(Material.REDSTONE_BLOCK)
            .setName("Delete")
            .addLore("Click to delete this warning");

        if (!rollbackCommands.isEmpty()) {
            itemStackBuilder.addLineLore();
            itemStackBuilder.addLore("&6Rollback actions:");
            for (String command : rollbackCommands) {
                itemStackBuilder.addLore("  - " + command);
            }
        }

        ItemStack item = StaffPlus.get().versionProtocol.addNbtString(
            Items.editor(itemStackBuilder.build())
                .setAmount(1)
                .build(), String.valueOf(warning.getId()));
        setItem(slot, item, action);
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