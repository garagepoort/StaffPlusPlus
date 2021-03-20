package net.shortninja.staffplus.domain.staff.warn.warnings.gui.actions;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.gui.IAction;
import net.shortninja.staffplus.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.domain.staff.warn.warnings.Warning;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ExpireWarningAction implements IAction {

    private final WarnService warnService = IocContainer.getWarnService();
    private final Warning warning;

    public ExpireWarningAction(Warning warning) {
        this.warning = warning;
    }

    @Override
    public void click(Player player, ItemStack item, int slot) {
        warnService.expireWarning(player, warning.getId());
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
