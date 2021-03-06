package net.shortninja.staffplus.staff.warn.appeals.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.gui.AbstractGui;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.warn.warnings.Warning;
import net.shortninja.staffplus.common.gui.IAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class AppealReasonSelectAction implements IAction {
    private final Options options = IocContainer.getOptions();
    private final Warning warning;
    private final Supplier<AbstractGui> previousGuiSupplier;

    public AppealReasonSelectAction(Warning warning, Supplier<AbstractGui> previousGuiSupplier) {
        this.warning = warning;
        this.previousGuiSupplier = previousGuiSupplier;
    }

    @Override
    public void click(Player player, ItemStack item, int slot) {
        if (warning.getAppeal().isPresent()) {
            return;
        }
        if (options.appealConfiguration.isFixedAppealReason()) {
            new AppealReasonSelectGui(warning, "Select your reason for appeal", previousGuiSupplier)
                .show(player);
        }
    }

    @Override
    public boolean shouldClose(Player player) {
        return false;
    }
}
