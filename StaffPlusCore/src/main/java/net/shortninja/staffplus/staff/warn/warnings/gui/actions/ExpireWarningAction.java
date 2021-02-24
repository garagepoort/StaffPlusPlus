package net.shortninja.staffplus.staff.warn.warnings.gui.actions;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.cmd.CommandUtil;
import net.shortninja.staffplus.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.staff.warn.warnings.Warning;
import net.shortninja.staffplus.unordered.IAction;
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
        CommandUtil.playerAction(player, () -> {
            warnService.expireWarning(player, warning.getId());
        });
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
