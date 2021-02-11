package net.shortninja.staffplus.staff.warn.warnings.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
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

import java.util.function.Supplier;

public class ManageWarningGui extends AbstractGui {
    private static final int SIZE = 54;

    private final WarnService warnService = IocContainer.getWarnService();
    private final Permission permission = IocContainer.getPermissionHandler();
    private final Options options = IocContainer.getOptions();
    private final Player player;
    private final Warning warning;
    private final Supplier<AbstractGui> goBack;

    public ManageWarningGui(Player player, String title, Warning warning, Supplier<AbstractGui> previousGuiSupplier) {
        super(SIZE, title, previousGuiSupplier);
        this.player = player;
        this.warning = warning;
        goBack = () -> new ManageWarningGui(player, title, warning, previousGuiSupplier);
    }

    @Override
    public void buildGui() {
        IAction deleteAction = new DeleteWarningAction();
        setItem(13, WarningItemBuilder.build(warning), null);

        if (permission.has(player, options.manageWarningsConfiguration.getPermissionDelete())) {
            addDeleteItem(warning, deleteAction, 8);
        }

        if (options.appealConfiguration.isEnabled() && permission.has(player, options.appealConfiguration.getCreateAppealPermission())) {
            if (!warning.getAppeal().isPresent()) {
                if (warning.getUuid().equals(player.getUniqueId())) {
                    addAppealItem(31);
                }
            } else {
                addAppealInfoItem(31);
            }
        }
    }

    private void addDeleteItem(Warning warning, IAction action, int slot) {
        ItemStack itemstack = Items.builder()
            .setMaterial(Material.REDSTONE_BLOCK)
            .setName("Delete")
            .addLore("Click to delete this warning")
            .build();

        ItemStack item = StaffPlus.get().versionProtocol.addNbtString(
            Items.editor(itemstack)
                .setAmount(1)
                .build(), String.valueOf(warning.getId()));
        setItem(slot, item, action);
    }

    private void addAppealItem(int slot) {
        IAction appealAction = null;
        if (!warning.getAppeal().isPresent()) {
            appealAction = options.appealConfiguration.isFixedAppealReason() ?
                new AppealReasonChatAction(warning) :
                new AppealReasonSelectAction(warning, goBack);
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
            action = new GoToManageAppealGuiAction(warning.getAppeal().get(), goBack);
        }

        setItem(slot, item, action);
    }

    private boolean canManageAppeals() {
        return permission.has(player, options.appealConfiguration.getApproveAppealPermission()) || permission.has(player, options.appealConfiguration.getRejectAppealPermission());
    }
}