package net.shortninja.staffplus.core.domain.staff.warn.warnings.gui.actions;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ExpireWarningAction implements IAction {

    private final WarnService warnService = StaffPlus.get().getIocContainer().get(WarnService.class);
    private final Warning warning;

    public ExpireWarningAction(Warning warning) {
        this.warning = warning;
    }

    @Override
    public void click(Player player, ItemStack item, int slot, ClickType clickType) {
        warnService.expireWarning(warning.getId());
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
