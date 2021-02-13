package net.shortninja.staffplus.staff.warn.appeals.gui.actions;

import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.staff.warn.appeals.Appeal;
import net.shortninja.staffplus.staff.warn.appeals.gui.ManageAppealGui;
import net.shortninja.staffplus.unordered.IAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class GoToManageAppealGuiAction implements IAction {
    private final Supplier<AbstractGui> previousGuiSupplier;
    private final Appeal appeal;

    public GoToManageAppealGuiAction(Appeal appeal, Supplier<AbstractGui> previousGuiSupplier) {
        this.appeal = appeal;
        this.previousGuiSupplier = previousGuiSupplier;
    }

    @Override
    public void click(Player player, ItemStack item, int slot) {
        new ManageAppealGui(player, "Manage warning appeal", appeal, previousGuiSupplier)
            .show(player);
    }

    @Override
    public boolean shouldClose(Player player) {
        return false;
    }
}
