package net.shortninja.staffplus.core.domain.staff.warn.appeals.gui.actions;

import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.Appeal;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.gui.ManageAppealGui;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class GoToManageAppealGuiAction implements IAction {
    private final Supplier<AbstractGui> previousGuiSupplier;
    private final Warning warning;
    private final Appeal appeal;

    public GoToManageAppealGuiAction(Warning warning, Appeal appeal, Supplier<AbstractGui> previousGuiSupplier) {
        this.warning = warning;
        this.appeal = appeal;
        this.previousGuiSupplier = previousGuiSupplier;
    }

    @Override
    public void click(Player player, ItemStack item, int slot, ClickType clickType) {
        new ManageAppealGui(player, "Manage warning appeal", warning, appeal, previousGuiSupplier)
            .show(player);
    }

    @Override
    public boolean shouldClose(Player player) {
        return false;
    }
}
