package net.shortninja.staffplus.core.domain.staff.warn.warnings.gui.actions;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class DeleteWarningAction implements IAction {

    private final WarnService warnService = StaffPlus.get().getIocContainer().get(WarnService.class);

    private final Warning warning;
    private final Supplier<AbstractGui> previousGuiSupplier;

    public DeleteWarningAction(Warning warning, Supplier<AbstractGui> previousGuiSupplier) {
        this.warning = warning;
        this.previousGuiSupplier = previousGuiSupplier;
    }

    @Override
    public void click(Player player, ItemStack item, int slot, ClickType clickType) {
        warnService.removeWarning(warning.getId());
        previousGuiSupplier.get().show(player);
    }

    @Override
    public boolean shouldClose(Player player) {
        return false;
    }
}
